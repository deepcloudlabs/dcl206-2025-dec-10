package com.example.application;

public record EnrichedTradeEvent(String symbol, double price, double quantity,double volume) {
	public EnrichedTradeEvent(TradeEvent event) {
		this(event.symbol(),event.price(),event.quantity(),event.price()*event.quantity());
	}
}
