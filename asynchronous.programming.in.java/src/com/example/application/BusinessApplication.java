package com.example.application;

import static com.example.BusinessService.shutdown;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.example.BusinessService;

public class BusinessApplication {
	public static void main(String[] args) {
		ExecutorService threadPool = Executors.newVirtualThreadPerTaskExecutor();
		System.out.println("[%s] Application is started.".formatted(Thread.currentThread().getName()));
		var businessService = new BusinessService();
		var result = businessService.fun();
		System.out.println("[%s] The result received from the service is %d".formatted(Thread.currentThread().getName(),result));
		
		System.out.println("[%s] Before we make async call!".formatted(Thread.currentThread().getName()));
		businessService.gun().thenAcceptAsync( response ->{
			System.out.println("[%s] The response received from the service is %d".formatted(Thread.currentThread().getName(),response));			
		},threadPool);
		System.out.println("[%s] After we make async call!".formatted(Thread.currentThread().getName()));

		for (var i=0;i<100;++i) {
			System.out.println("[%s] Working on another task[%d] in main!".formatted(Thread.currentThread().getName(),i));
			try {TimeUnit.MILLISECONDS.sleep(50);}catch(Exception e) {}
		}
		System.out.println("[%s] Application is completed.".formatted(Thread.currentThread().getName()));
		shutdown();
		threadPool.shutdown();
	}

}
