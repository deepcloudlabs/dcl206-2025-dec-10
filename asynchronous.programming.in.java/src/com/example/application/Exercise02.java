package com.example.application;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Gatherers;

public class Exercise02 {
	private static AtomicInteger counter = new AtomicInteger(0);
	private static Pattern PRICE_PATTERN = Pattern.compile("\"price\"\\s*:\\s*\"([+-]?\\d+(?:\\.\\d+)?)\"");
	
	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("Application is just started.");
		var request = HttpRequest.newBuilder(URI.create("https://api.binance.com/api/v3/ticker/price")).build();
		var httpClient = HttpClient.newBuilder().build();
		var response = httpClient.send(request,BodyHandlers.ofString())
		                         .body();
		var pattern = Pattern.compile("\"symbol\"\\s*:\\s*\"([^\"]+)\"");
		var matcher = pattern.matcher(response);
		List<String> symbols = new ArrayList<>();
		while (matcher.find()) {
			symbols.add(matcher.group(1));
		}
		var tickers = symbols.stream()
				             .parallel()
				             .sorted()
				             .sequential()
				             .gather(Gatherers.mapConcurrent(10, _ -> getSyncTicker(httpClient,"BTCUSDT")))
				             .gather(Gatherers.windowFixed(10))
				             .map( window -> window.stream().collect(Collectors.summarizingDouble(Exercise02::getPrice)))
				             .peek(System.out::println)
				             .toList();
		tickers.forEach(System.out::println);
	}
	
	public static double getPrice(String json) {
		var matcher = PRICE_PATTERN.matcher(json);
		if (!matcher.find())
			throw new IllegalArgumentException("price not found");
		return Double.parseDouble(matcher.group(1));
	}
	
	public static String getSyncTicker(HttpClient httpClient,String symbol) {
		System.err.println("[getSyncTicker] %s is making a rest api call.".formatted(Thread.currentThread().getName()));
		var req = HttpRequest.newBuilder(URI.create("https://api.binance.com/api/v3/ticker/price?symbol=%s".formatted(symbol))).build();
		String res;
		try {
			res = httpClient.send(req,BodyHandlers.ofString()).body();
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException(e.getMessage());
		}
		return res;
	}
}
