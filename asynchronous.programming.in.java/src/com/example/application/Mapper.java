package com.example.application;

import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;
import java.util.function.Function;

public class Mapper<T, R> extends SubmissionPublisher<R> implements Flow.Processor<T, R> {
	private final Function<T,R> mapFunction;
	private Flow.Subscription subscription;
	
	public Mapper(Function<T, R> mapFunction) {
		this.mapFunction = mapFunction;
	}

	@Override
	public void onSubscribe(Subscription subscription) {
		this.subscription = subscription;
		this.subscription.request(1);
	}

	@Override
	public void onNext(T event) {
		submit(mapFunction.apply(event));
		this.subscription.request(1);
	}

	@Override
	public void onError(Throwable throwable) {

	}

	@Override
	public void onComplete() {
		close();
	}

}
