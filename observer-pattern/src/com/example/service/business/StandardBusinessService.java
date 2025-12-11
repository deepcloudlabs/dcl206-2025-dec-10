package com.example.service.business;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.example.service.BusinessService;

public class StandardBusinessService implements BusinessService {

	@Override
	public CompletableFuture<Integer> fun() {
		return CompletableFuture.supplyAsync(()->{
			try {TimeUnit.SECONDS.sleep(5);} catch (InterruptedException e) {}
			return 42;
		});
	}

}
