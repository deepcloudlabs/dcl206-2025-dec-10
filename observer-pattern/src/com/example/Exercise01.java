package com.example;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

import com.example.service.business.StandardBusinessService;

public class Exercise01 {
	public static void main(String[] args) {
		var businessService = new StandardBusinessService();
		Function<Integer,Integer> observer1 = result -> {
			System.out.println(result);
			return result * result;
		};
		
		Consumer<Integer> observer2 = System.out::println;
		businessService.fun().thenApplyAsync(observer1)
		                     .thenAcceptAsync(observer2);
		for (var i=0;i<100;++i) {
			try {TimeUnit.MILLISECONDS.sleep(55);} catch (InterruptedException e) {}
			System.err.println("for: %d".formatted(i));	
		}
			
	}
}
