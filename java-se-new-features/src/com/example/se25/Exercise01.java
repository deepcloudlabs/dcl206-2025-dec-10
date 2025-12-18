package com.example.se25;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.StructuredTaskScope;

public class Exercise01 {

    // Request context: ThreadLocal yerine, scope içinde “capability” gibi davranır.
    private static final ScopedValue<String> CORRELATION_ID = ScopedValue.newInstance();

    public static void main(String[] args) throws Exception {
        for (var i=1_000;i<10_000;++i) {
        	var customerId = i;
        	String correlationId = UUID.randomUUID().toString();
        	
        	ScopedValue.where(CORRELATION_ID, correlationId).run(() -> {
        		try {
        			var response = handleRequest("cust-%d".formatted(customerId));
        			System.out.println(response);
        		} catch (Exception e) {
        			throw new RuntimeException(e);
        		}
        	});        	
        }
    }

    static Response handleRequest(String customerId) throws Exception {
        // StructuredTaskScope (JDK 25 preview): fork/join lifecycle bu blokla sınırlı.
        try (var scope = StructuredTaskScope.open()) {

            var customerTask = scope.fork(() -> fetchCustomer(customerId));
            var riskTask     = scope.fork(() -> computeRiskScore(customerId));

            scope.join();

            return new Response(customerTask.get(), riskTask.get(), CORRELATION_ID.get());
        }
    }

    static Customer fetchCustomer(String customerId) throws InterruptedException {
        log("fetchCustomer started");
        Thread.sleep(Duration.ofMillis(120)); // I/O simülasyonu
        log("fetchCustomer done");
        return new Customer(customerId, "ACME Corp");
    }

    static int computeRiskScore(String customerId) throws InterruptedException {
        log("computeRiskScore started");
        Thread.sleep(Duration.ofMillis(180)); // I/O simülasyonu
        log("computeRiskScore done");
        return 42;
    }

    static void log(String msg) {
        System.out.printf("[cid=%s] %s%n", CORRELATION_ID.get(), msg);
    }

    record Customer(String id, String name) {}
    record Response(Customer customer, int riskScore, String correlationId) {}
}
