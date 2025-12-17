package com.example.application;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.regex.Pattern;

public class Exercise06 {
	private static Pattern PRICE_PATTERN = Pattern.compile("\"price\"\\s*:\\s*\"([+-]?\\d+(?:\\.\\d+)?)\"");
	private static final String TICKER_URL= "https://api.binance.com/api/v3/ticker/price?symbol=%s";
	private static final String ALL_TICKERS_URL= "https://api.binance.com/api/v3/ticker/price";
	public static void main(String[] args) {
		try (var threadPool = Executors.newVirtualThreadPerTaskExecutor()) {
			HttpClient client = HttpClient.newBuilder().executor(threadPool).build();
			SubmissionPublisher<String> symbolPublisher = new SubmissionPublisher<>();
			var fetchTickers = new MapAsyncProcessor<String, String>(
					threadPool, 1024, 10, symbol -> fetchTickerAsync(client,symbol)	
			);
			
			var window = new WindowFixedProcessor<String>(exec,128,10);
			var summarize = new SummarizePricesProcessor(exec,128);
			var sink = new PrintingSubscriber();
			
			// wiring Reactive Stream/Pipeline
			symbolPublisher.subscribe(fetchTickers);
			fetchTickers.subscribe(window);
			window.subscribe(summarize);
			summarize.subscribe(sink);
			
			fetchSymbolsAsync(client)
			          .thenAccept( symbols -> {
			        	  parseSymbols(symbols).stream().sorted().forEach(symbolPublisher::submit);
			        	  symbolPublisher.close();
			          }).exceptionally( err -> {
			        	  symbolPublisher.closeExceptionally(err); 
			        	  return null;
			          });
			sink.await();
		}

	}
	
    public static CompletableFuture<String> fetchSymbolsAsync(HttpClient client) {
    	var req = HttpRequest.newBuilder(URI.create(ALL_TICKERS_URL))
    			             .GET()
    			             .build();
    	return client.sendAsync(req, BodyHandlers.ofString())
    			     .thenApply(HttpResponse::body);
    }

	
    public static CompletableFuture<String> fetchTickerAsync(HttpClient client,String symbol) {
    	var req = HttpRequest.newBuilder(URI.create(TICKER_URL.formatted(symbol)))
    			             .GET()
    			             .build();
    	return client.sendAsync(req, BodyHandlers.ofString())
    			     .thenApply(HttpResponse::body);
    }
    
    public static List<String> parseSymbols(String jsonArray){
		var pattern = Pattern.compile("\"symbol\"\\s*:\\s*\"([^\"]+)\"");
		var matcher = pattern.matcher(jsonArray);
		List<String> symbols = new ArrayList<>();
		while (matcher.find()) {
			symbols.add(matcher.group(1));
		}
		symbols.sort(String::compareTo);    
		return symbols;
    }
    
    public static double getPrice(String jsonObject) {
		var matcher = PRICE_PATTERN.matcher(jsonObject);
		if (!matcher.find())
			throw new IllegalArgumentException("price not found");
		return Double.parseDouble(matcher.group(1));
	}
    
	static final class MapAsyncProcessor<IN, OUT> extends SubmissionPublisher<OUT> implements Flow.Processor<IN, OUT> {
		private Subscription upstream;
		private final Semaphore permits;
		private final Function<IN, CompletableFuture<OUT>> mapperFunction;
		private final AtomicBoolean terminated = new AtomicBoolean(false);

		public MapAsyncProcessor(Executor executor, int buffer, int concurrency,
				Function<IN, CompletableFuture<OUT>> mapperFunction) {
			super(executor, buffer);
			this.permits = new Semaphore(concurrency);
			this.mapperFunction = mapperFunction;
		}

		@Override
		public void onSubscribe(Subscription subscription) {
			this.upstream = subscription;
			subscription.request(permits.availablePermits());
		}

		@Override
		public void onNext(IN item) {
			if (terminated.get()) return;
			try {
				permits.acquire();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				onError(e);
				return;
			}
			CompletableFuture<OUT> futureResponse;
			futureResponse = mapperFunction.apply(item);
			futureResponse.whenComplete((out,err)->{
				this.submit(out);
			});
		}

		@Override
		public void onError(Throwable throwable) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onComplete() {
			if(terminated.compareAndSet(false, true)) {
				this.close();
			}
		}

	}
}
