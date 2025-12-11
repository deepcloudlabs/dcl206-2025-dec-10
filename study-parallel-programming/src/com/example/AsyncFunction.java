package com.example;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AsyncFunction {
	private static Executor executor = Executors.newFixedThreadPool(10);
	
	public static void main(String[] args) {
		// int result = fun();
        gun().thenAcceptAsync(response -> {
        	System.out.println("[%s] Result is available: %d".formatted(Thread.currentThread().getName(),response));
        });
        for (var i=0;i<100;++i) {
        	System.out.println("[%s] i: %d".formatted(Thread.currentThread().getName(), i));
        	try {TimeUnit.MILLISECONDS.sleep(500);}catch (Exception e) {}
        }
	}

	public static int fun() {
		try {TimeUnit.SECONDS.sleep(10);}catch (Exception e) {}
		return 42;
	}
	public static CompletableFuture<Integer> gun() {
		return CompletableFuture.supplyAsync(()->{
			System.out.println("[%s] gun() is running...".formatted(Thread.currentThread().getName()));
			try {TimeUnit.SECONDS.sleep(10);}catch (Exception e) {}
			return 42;			
		},executor);
	}	
}
