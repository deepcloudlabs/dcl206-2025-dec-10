package com.example.service.solution;

public interface A {
	public abstract void fun();
	default void gun() { // since java 8 
		run();
		System.err.println("gun() is running...");
	}
	default void hun() { // since java 8 
		run();
		System.err.println("gun() is running...");
	}
	public static void sun() { // since java 8
		cun();
		System.err.println("sun() is running...");		
	}
	private void run() { // since java 9
		System.err.println("run() is running...");				
	}
	private static void cun() { // since java 9
		System.err.println("cun() is running...");						
	}
}
