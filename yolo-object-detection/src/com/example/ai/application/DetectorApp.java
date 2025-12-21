package com.example.ai.application;

import ai.onnxruntime.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.FloatBuffer;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class DetectorApp {

	private static final String[] COCO80 = new String[] { "person", "bicycle", "car", "motorcycle", "airplane", "bus",
			"train", "truck", "boat", "traffic light", "fire hydrant", "stop sign", "parking meter", "bench", "bird",
			"cat", "dog", "horse", "sheep", "cow", "elephant", "bear", "zebra", "giraffe", "backpack", "umbrella",
			"handbag", "tie", "suitcase", "frisbee", "skis", "snowboard", "sports ball", "kite", "baseball bat",
			"baseball glove", "skateboard", "surfboard", "tennis racket", "bottle", "wine glass", "cup", "fork",
			"knife", "spoon", "bowl", "banana", "apple", "sandwich", "orange", "broccoli", "carrot", "hot dog", "pizza",
			"donut", "cake", "chair", "couch", "potted plant", "bed", "dining table", "toilet", "tv", "laptop", "mouse",
			"remote", "keyboard", "cell phone", "microwave", "oven", "toaster", "sink", "refrigerator", "book", "clock",
			"vase", "scissors", "teddy bear", "hair drier", "toothbrush" };

	public static void main(String[] args) throws Exception {
		System.setProperty("java.awt.headless", "true"); 

		Map<String, String> cli = parseArgs(args);
		String modelPath = require(cli, "--model");
		String imagePath = require(cli, "--image");
		String outPath = cli.getOrDefault("--out", "out.jpg");

		float confThreshold = Float.parseFloat(cli.getOrDefault("--conf", "0.25"));
		float iouThreshold = Float.parseFloat(cli.getOrDefault("--iou", "0.45"));
		int inputSize = Integer.parseInt(cli.getOrDefault("--size", "640")); // typical YOLO size

		BufferedImage image = ImageIO.read(new File(imagePath));
		if (image == null)
			throw new IllegalArgumentException("Could not read image: " + imagePath);

		try (OrtEnvironment env = OrtEnvironment.getEnvironment();
				OrtSession session = env.createSession(modelPath, new OrtSession.SessionOptions())) {

			// Preprocess (letterbox + normalize + CHW)
			LetterboxResult lb = letterbox(image, inputSize, inputSize, 114);

			float[] chw = toCHWFloat(lb.letterboxedRgb, inputSize, inputSize);

			// Build input tensor
			String inputName = session.getInputNames().iterator().next();
			long[] inputShape = new long[] { 1, 3, inputSize, inputSize };
			try (OnnxTensor inputTensor = OnnxTensor.createTensor(env, FloatBuffer.wrap(chw), inputShape)) {

				Map<String, OnnxTensor> inputs = Map.of(inputName, inputTensor);

				// Run inference
				List<Detection> detections;
				try (OrtSession.Result result = session.run(inputs)) {
					Object outputVal = result.get(0).getValue();
					detections = decodeYoloOutput(outputVal, lb, inputSize, inputSize, confThreshold);
				}

				// NMS (class-aware)
				detections = nmsPerClass(detections, iouThreshold);

				// Draw + save
				BufferedImage annotated = drawDetections(image, detections);
				ImageIO.write(annotated, "jpg", new File(outPath));

				// Emit detections (operationally friendly)
				System.out.println("{" + "\"model\":\"" + safe(modelPath) + "\"," + "\"image\":\"" + safe(imagePath)
						+ "\"," + "\"out\":\"" + safe(outPath) + "\"," + "\"count\":" + detections.size() + "}");
				for (Detection d : detections) {
					System.out.println(d.toJsonLine());
				}
			}
		}
	}

	static void dumpModelIO(OrtSession session) throws OrtException {
		  System.out.println("Inputs:");
		  session.getInputInfo().forEach((name, nodeInfo) -> {
		    TensorInfo ti = (TensorInfo) nodeInfo.getInfo();
		    System.out.printf("  %s -> type=%s onnxType=%s shape=%s%n",
		        name, ti.type, ti.onnxType, Arrays.toString(ti.getShape()));
		  });

		  System.out.println("Outputs:");
		  session.getOutputInfo().forEach((name, nodeInfo) -> {
		    TensorInfo ti = (TensorInfo) nodeInfo.getInfo();
		    System.out.printf("  %s -> type=%s onnxType=%s shape=%s%n",
		        name, ti.type, ti.onnxType, Arrays.toString(ti.getShape()));
		  });
		}
	
	static class LetterboxResult {
		final BufferedImage letterboxedRgb;
		final int origW, origH;
		final int newW, newH;
		final int padX, padY;
		final float scale;

		LetterboxResult(BufferedImage letterboxedRgb, int origW, int origH, int newW, int newH, int padX, int padY,
				float scale) {
			this.letterboxedRgb = letterboxedRgb;
			this.origW = origW;
			this.origH = origH;
			this.newW = newW;
			this.newH = newH;
			this.padX = padX;
			this.padY = padY;
			this.scale = scale;
		}
	}

	/** Letterbox resize to targetW x targetH with constant padding. */
	static LetterboxResult letterbox(BufferedImage src, int targetW, int targetH, int padColor) {
		int origW = src.getWidth();
		int origH = src.getHeight();

		float r = Math.min(targetW / (float) origW, targetH / (float) origH);
		int newW = Math.round(origW * r);
		int newH = Math.round(origH * r);

		int padX = (targetW - newW) / 2;
		int padY = (targetH - newH) / 2;

		BufferedImage out = new BufferedImage(targetW, targetH, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = out.createGraphics();
		try {
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.setColor(new Color(padColor, padColor, padColor));
			g.fillRect(0, 0, targetW, targetH);
			g.drawImage(src, padX, padY, newW, newH, null);
		} finally {
			g.dispose();
		}

		// Ensure RGB order in a standard buffer (we’ll read pixels via getRGB anyway).
		BufferedImage rgb = new BufferedImage(targetW, targetH, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = rgb.createGraphics();
		try {
			g2.drawImage(out, 0, 0, null);
		} finally {
			g2.dispose();
		}

		return new LetterboxResult(rgb, origW, origH, newW, newH, padX, padY, r);
	}

	/** Convert BufferedImage to float32 CHW in [0,1], RGB. */
	static float[] toCHWFloat(BufferedImage img, int w, int h) {
		float[] out = new float[3 * w * h];
		int idxR = 0;
		int idxG = w * h;
		int idxB = 2 * w * h;

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int rgb = img.getRGB(x, y);
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = rgb & 0xFF;

				out[idxR++] = r / 255.0f;
				out[idxG++] = g / 255.0f;
				out[idxB++] = b / 255.0f;
			}
		}
		return out;
	}

	// -----------------------------
	// Postprocessing (YOLO decode + NMS)
	// -----------------------------

	static class Detection {
		final int classId;
		final String label;
		final float confidence;
		final float x1, y1, x2, y2; // in original image coordinates

		Detection(int classId, String label, float confidence, float x1, float y1, float x2, float y2) {
			this.classId = classId;
			this.label = label;
			this.confidence = confidence;
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}

		float area() {
			return Math.max(0, x2 - x1) * Math.max(0, y2 - y1);
		}

		String toJsonLine() {
			return "{" + "\"classId\":" + classId + "," + "\"label\":\"" + safe(label) + "\"," + "\"confidence\":"
					+ String.format(Locale.US, "%.5f", confidence) + "," + "\"box\":["
					+ String.format(Locale.US, "%.2f", x1) + "," + String.format(Locale.US, "%.2f", y1) + ","
					+ String.format(Locale.US, "%.2f", x2) + "," + String.format(Locale.US, "%.2f", y2) + "]" + "}";
		}
	}

	/**
	 * Decode common YOLO ONNX outputs into detections in original image
	 * coordinates. Supports: - YOLOv5-style: [1, N, 5+numClasses] =>
	 * (cx,cy,w,h,obj, class_probs...) - YOLOv8-style: [1, (4+numClasses), N] =>
	 * (cx,cy,w,h, class_scores...)
	 */
	static List<Detection> decodeYoloOutput(Object outputVal, LetterboxResult lb, int inputW, int inputH,
			float confThreshold) {

		float[][][] out3;
		if (outputVal instanceof float[][][] o) {
			out3 = o;
		} else if (outputVal instanceof float[][] o2) {
			out3 = new float[][][] { o2 };
		} else {
			throw new IllegalStateException("Unsupported output type: " + outputVal.getClass());
		}

		if (out3.length < 1)
			return List.of();

		int d1 = out3[0].length; // could be N or (4+classes)
		int d2 = out3[0][0].length; // could be (5+classes) or N

		List<Detection> dets = new ArrayList<>();

		if (d2 >= 6) {
			int N = d1;
			int M = d2;
			int numClasses = M - 5;

			for (int i = 0; i < N; i++) {
				float cx = out3[0][i][0];
				float cy = out3[0][i][1];
				float w = out3[0][i][2];
				float h = out3[0][i][3];
				float obj = out3[0][i][4];

				int bestClass = -1;
				float bestProb = 0f;
				for (int c = 0; c < numClasses; c++) {
					float p = out3[0][i][5 + c];
					if (p > bestProb) {
						bestProb = p;
						bestClass = c;
					}
				}

				float conf = obj * bestProb;
				if (conf < confThreshold)
					continue;

				Box box = toXyxyInOriginal(cx, cy, w, h, lb, inputW, inputH);
				dets.add(new Detection(bestClass, labelOf(bestClass), conf, box.x1, box.y1, box.x2, box.y2));
			}
			return dets;
		} else if (d1 >= 6) {
			int C = d1; // 4+classes
			int N = d2;
			int numClasses = C - 4;

			for (int i = 0; i < N; i++) {
				float cx = out3[0][0][i];
				float cy = out3[0][1][i];
				float w = out3[0][2][i];
				float h = out3[0][3][i];

				int bestClass = -1;
				float bestScore = 0f;
				for (int c = 0; c < numClasses; c++) {
					float s = out3[0][4 + c][i];
					if (s > bestScore) {
						bestScore = s;
						bestClass = c;
					}
				}

				float conf = bestScore;
				if (conf < confThreshold)
					continue;

				Box box = toXyxyInOriginal(cx, cy, w, h, lb, inputW, inputH);
				dets.add(new Detection(bestClass, labelOf(bestClass), conf, box.x1, box.y1, box.x2, box.y2));
			}
			return dets;
		}

		throw new IllegalStateException("Cannot infer YOLO output layout from shape: [1," + d1 + "," + d2 + "]");
	}

	static class Box {
		final float x1, y1, x2, y2;

		Box(float x1, float y1, float x2, float y2) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}
	}

	/**
	 * Convert YOLO (cx,cy,w,h) in model input coordinate system to original image
	 * XYXY. Also handles normalized outputs (0..1) by scaling to inputW/inputH.
	 */
	static Box toXyxyInOriginal(float cx, float cy, float w, float h, LetterboxResult lb, int inputW, int inputH) {

		boolean looksNormalized = (cx >= 0 && cx <= 1.5f) && (cy >= 0 && cy <= 1.5f) && (w >= 0 && w <= 1.5f)
				&& (h >= 0 && h <= 1.5f);
		if (looksNormalized) {
			cx *= inputW;
			cy *= inputH;
			w *= inputW;
			h *= inputH;
		}

		float x1 = cx - w / 2f;
		float y1 = cy - h / 2f;
		float x2 = cx + w / 2f;
		float y2 = cy + h / 2f;

		x1 = (x1 - lb.padX) / lb.scale;
		y1 = (y1 - lb.padY) / lb.scale;
		x2 = (x2 - lb.padX) / lb.scale;
		y2 = (y2 - lb.padY) / lb.scale;

		x1 = clamp(x1, 0, lb.origW - 1);
		y1 = clamp(y1, 0, lb.origH - 1);
		x2 = clamp(x2, 0, lb.origW - 1);
		y2 = clamp(y2, 0, lb.origH - 1);

		return new Box(x1, y1, x2, y2);
	}

	static float clamp(float v, float lo, float hi) {
		return Math.max(lo, Math.min(hi, v));
	}

	static float iou(Detection a, Detection b) {
		float ix1 = Math.max(a.x1, b.x1);
		float iy1 = Math.max(a.y1, b.y1);
		float ix2 = Math.min(a.x2, b.x2);
		float iy2 = Math.min(a.y2, b.y2);

		float iw = Math.max(0, ix2 - ix1);
		float ih = Math.max(0, iy2 - iy1);
		float inter = iw * ih;
		float union = a.area() + b.area() - inter;
		if (union <= 0)
			return 0f;
		return inter / union;
	}

	static List<Detection> nmsPerClass(List<Detection> dets, float iouThreshold) {
		Map<Integer, List<Detection>> byClass = dets.stream().collect(Collectors.groupingBy(d -> d.classId));

		List<Detection> out = new ArrayList<>();
		for (Map.Entry<Integer, List<Detection>> e : byClass.entrySet()) {
			List<Detection> cls = new ArrayList<>(e.getValue());
			cls.sort((a, b) -> Float.compare(b.confidence, a.confidence));

			boolean[] removed = new boolean[cls.size()];
			for (int i = 0; i < cls.size(); i++) {
				if (removed[i])
					continue;
				Detection keep = cls.get(i);
				out.add(keep);

				for (int j = i + 1; j < cls.size(); j++) {
					if (removed[j])
						continue;
					if (iou(keep, cls.get(j)) > iouThreshold) {
						removed[j] = true;
					}
				}
			}
		}

		out.sort((a, b) -> Float.compare(b.confidence, a.confidence));
		return out;
	}

	static BufferedImage drawDetections(BufferedImage src, List<Detection> dets) {
		BufferedImage out = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = out.createGraphics();
		try {
			g.drawImage(src, 0, 0, null);
			g.setStroke(new BasicStroke(2.0f));

			for (Detection d : dets) {
				g.setColor(Color.RED);
				int x = Math.round(d.x1);
				int y = Math.round(d.y1);
				int w = Math.round(d.x2 - d.x1);
				int h = Math.round(d.y2 - d.y1);

				g.drawRect(x, y, w, h);

				String text = d.label + " " + String.format(Locale.US, "%.2f", d.confidence);
				drawLabel(g, text, x, y);
			}
		} finally {
			g.dispose();
		}
		return out;
	}

	static void drawLabel(Graphics2D g, String text, int x, int y) {
		Font font = new Font("SansSerif", Font.BOLD, 14);
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();

		int pad = 4;
		int textW = fm.stringWidth(text);
		int textH = fm.getHeight();

		int bx = x;
		int by = Math.max(0, y - textH);

		g.setColor(new Color(255, 0, 0, 200));
		g.fillRect(bx, by, textW + pad * 2, textH);

		g.setColor(Color.WHITE);
		g.drawString(text, bx + pad, by + fm.getAscent());
	}

	static String labelOf(int classId) {
		if (classId >= 0 && classId < COCO80.length)
			return COCO80[classId];
		return "class_" + classId;
	}

	static Map<String, String> parseArgs(String[] args) {
		Map<String, String> map = new HashMap<>();
		for (int i = 0; i < args.length; i++) {
			String k = args[i];
			if (!k.startsWith("--"))
				continue;
			String v = (i + 1 < args.length && !args[i + 1].startsWith("--")) ? args[++i] : "true";
			map.put(k, v);
		}
		return map;
	}

	static String require(Map<String, String> cli, String key) {
		String v = cli.get(key);
		if (v == null || v.isBlank())
			throw new IllegalArgumentException("Missing required arg: " + key);
		return v;
	}

	static String safe(String s) {
		return s.replace("\\", "\\\\").replace("\"", "\\\"");
	}
}
