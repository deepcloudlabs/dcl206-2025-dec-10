package com.example.application;

import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

public class Exercise04 {

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
		var publisher = new SubmissionPublisher<TradeEvent>();
		var volumeMapper = new Mapper<TradeEvent,EnrichedTradeEvent>(EnrichedTradeEvent::new);
		publisher.subscribe(volumeMapper);
		volumeMapper.subscribe(new SlowSubscriber());
		volumeMapper.subscribe(new FastSubscriber());
		tradeEvents.forEach(publisher::submit);
		System.err.println("Application is just completed.");
		try {TimeUnit.SECONDS.sleep(30);}catch(Exception e) {}
	}

}

class SlowSubscriber implements Flow.Subscriber<EnrichedTradeEvent> {

	private Subscription subscription;

	@Override
	public void onSubscribe(Subscription subscription) {
		this.subscription = subscription;
		System.err.println("SlowSubscriber has just subscribed!");
		subscription.request(1);
	}

	@Override
	public void onNext(EnrichedTradeEvent event) {
		System.err.println("[%s] SlowSubscriber has just received new event: %s".formatted(Thread.currentThread().getName(),event));	
		try {TimeUnit.SECONDS.sleep(5);}catch(Exception e) {}
		System.err.println("[%s] SlowSubscriber has finally processed the event: %s".formatted(Thread.currentThread().getName(),event));
		subscription.request(1);
	}

	@Override
	public void onError(Throwable t) {
		System.err.println("[SlowSubscriber] An error has occured: %s".formatted(t.getMessage()));
	}

	@Override
	public void onComplete() {
		System.err.println("[SlowSubscriber] Completed.");
	}
	
}

class FastSubscriber implements Flow.Subscriber<EnrichedTradeEvent> {

	private Subscription subscription;

	@Override
	public void onSubscribe(Subscription subscription) {
		this.subscription = subscription;
		System.err.println("FastSubscriber has just subscribed!");
		subscription.request(1);
	}

	@Override
	public void onNext(EnrichedTradeEvent event) {
		System.err.println("[%s] FastSubscriber has just received new event: %s".formatted(Thread.currentThread().getName(),event));	
		System.err.println("[%s] FastSubscriber has finally processed the event: %s".formatted(Thread.currentThread().getName(),event));
		subscription.request(1);
	}

	@Override
	public void onError(Throwable t) {
		System.err.println("[FastSubscriber] An error has occured: %s".formatted(t.getMessage()));
	}

	@Override
	public void onComplete() {
		System.err.println("[FastSubscriber] Completed.");
	}
	
}