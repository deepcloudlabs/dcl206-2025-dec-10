javac -cp onnxruntime.jar DetectorApp.java
java  -cp .:onnxruntime.jar DetectorApp --model yolov3-tiny.onnx --image src/input.jpg --out src/out.jpg --size 640 --conf 0.25 --iou 0.45