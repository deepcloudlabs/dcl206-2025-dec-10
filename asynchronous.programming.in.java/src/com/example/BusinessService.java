package com.example;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BusinessService {
	private static final ExecutorService threadPool = 
			//Executors.newSingleThreadExecutor();
			//Executors.newCachedThreadPool();
	        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	public static void shutdown() {
		threadPool.shutdown();
	}
	public int fun() {
		try {TimeUnit.SECONDS.sleep(3);}catch(Exception e) {}
		return 42;
	}
	
	public CompletableFuture<Integer>  gun() {
		return CompletableFuture.supplyAsync(() ->{
			System.out.println("[%s] Async function is started.".formatted(Thread.currentThread().getName()));
			try {TimeUnit.SECONDS.sleep(3);}catch(Exception e) {}
			return 42;			
		},threadPool);
	}
	
}
