package com.example.application;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

public class Exercise03 {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		System.err.println("Application is just started.");
		var tradeEvents = List.of(
				new TradeEvent("orcl", 100,200),
				new TradeEvent("orcl", 101,100),
				new TradeEvent("orcl", 102,200),
				new TradeEvent("orcl", 103,100),
				new TradeEvent("orcl", 104,200)
			);
		var observable = new TradeObservable();
		Observer slowObserver = (o,event) -> {
			System.err.println("SlowObserver has received the event: %s".formatted(event));
			try {TimeUnit.SECONDS.sleep(5);}catch(Exception e) {}
			System.err.println("SlowObserver has finally processed the event: %s".formatted(event));
		} ;
		Observer fastObserver = (o,event) -> {
			System.err.println("FastObserver has received the event: %s".formatted(event));
			System.err.println("FastObserver has finally processed the event: %s".formatted(event));
		} ;
		observable.addObserver(slowObserver);
		observable.addObserver(fastObserver);
		tradeEvents.forEach(observable::notifyObservers);
		System.err.println("Application is just completed.");
	}

}

class TradeObservable extends Observable {

	@Override
	public void notifyObservers(Object event) {
		setChanged();
		super.notifyObservers(event);
	}
	
}