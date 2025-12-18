package com.example.se19;

import java.util.concurrent.Executors;

public class Exercise02 {

	public static void main(String[] args) throws InterruptedException { // -Xss4m
		Thread t1 = new Thread(() -> { // Platform Thread -> Kernel
			System.out.println("Hello Mars!");
		}); // Stack: 4m
		Thread t2 = new Thread(() -> {
			System.out.println("Hello Jupiter!");
		}); // Stack: 4m
		t1.start();
		t2.start();
		//1024*1024 -> Thread -> 1024 * 1024 * 4m = 4tb -> virtual memory
		t1.join();
		t2.join();
		// API Layer -> request-response
		// request -- submit --> thread pool: thread accepts request
		// Thread.ofPlatform();
	    Thread.ofVirtual(); // 1k
	    //1024*1024 -> Thread.ofVirtual() -> 1024 * 1024 * 1k = 1gb -> virtual memory
	    try (var threadPool = Executors.newVirtualThreadPerTaskExecutor()){
	    	for (int i=0;i<10_000;++i) {
	    		int threadId = i;
	    		threadPool.submit(() ->{
	    			System.out.println("Task %d is running on %s".formatted(threadId,Thread.currentThread()));
	    		});
	    	}
	    }
	}

}
