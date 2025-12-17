package com.example.application;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Listener;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

public class Exercise05 {

	public static void main(String[] args) throws InterruptedException {
		var listener = new BinanceWebSocketListener();
		HttpClient.newHttpClient()
		          .newWebSocketBuilder()
		          .buildAsync(URI.create("wss://stream.binance.com:9443/ws/btcusdt@trade"), listener);
		          
		TimeUnit.SECONDS.sleep(60);
	}

}

class BinanceWebSocketListener implements Listener {

	@Override
	public void onOpen(WebSocket webSocket) {
		System.err.println("Connected to Binance WebSocket Server.");
		webSocket.request(1);
	}

	@Override
	public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
		System.err.println("Session [%s]: %s".formatted(webSocket,data.toString()));
		webSocket.request(1);
		return null;
	}

	@Override
	public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
		System.err.println("Connection is closed!"); 
		return Listener.super.onClose(webSocket, statusCode, reason);
	}

	@Override
	public void onError(WebSocket webSocket, Throwable error) {
		System.err.println("An error has occured while websocket communication: %s".formatted(error.getMessage())); 
	}
	
}