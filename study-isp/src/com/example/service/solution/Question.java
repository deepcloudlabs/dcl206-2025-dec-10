package com.example.service.solution;

public class Question {

	public static void main(String[] args) {
		U u = new U();
		u.fun();

	}

}

interface P {
	default void fun() {
		System.err.println("P::fun() is running...");
	}
	void gun();
}

interface Q {
	default void fun() {
		System.err.println("Q::fun() is running...");
	}
	void gun();
}

class U implements P, Q {

	@Override
	public void fun() {
		((Q)this).fun();
	}

	@Override
	public void gun() {
		// TODO Auto-generated method stub
		
	}
	
}