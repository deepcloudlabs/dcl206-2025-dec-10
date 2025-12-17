package com.example.application;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class Exercise01 {
	private static AtomicInteger counter = new AtomicInteger(0);
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
		symbols.sort(String::compareTo);
		//symbols.forEach(System.out::println);
		System.out.println(symbols.size());
		var start = System.currentTimeMillis();
		symbols.stream().limit(100).forEach(
				symbol -> {
					var req = HttpRequest.newBuilder(URI.create("https://api.binance.com/api/v3/ticker/price?symbol=%s".formatted(symbol))).build();
					httpClient.sendAsync(req,BodyHandlers.ofString()).thenAcceptAsync( res -> {
						System.out.println(res.body());		
						if (counter.incrementAndGet() == 100) {
							var stop = System.currentTimeMillis();
							System.out.println("Application is just completed: %d ms.".formatted(stop-start));							
						}
					});
				}
		);
		
		// System.out.println("Response: %s".formatted(response));
		TimeUnit.SECONDS.sleep(15);
	}

}
