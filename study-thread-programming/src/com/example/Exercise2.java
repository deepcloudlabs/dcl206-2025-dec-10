package com.example;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Exercise2 {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		// -Xss128k
		// Thread.ofPlatform() -> Thread
		// Thread.ofVirtual()  -> Platform/Developer
		var task2 = new Task2();
		var futureTask = new FutureTask<>(task2);
		Thread t2 = new Thread(futureTask);
		t2.start();
		var result = futureTask.get(); // synchronization + result
		System.out.println(result);
	}

}

class Task2 implements Callable<Integer> {
	@Override
	public Integer call() {
		System.out.println("Hello Moon!");
		return 108;
	}

}