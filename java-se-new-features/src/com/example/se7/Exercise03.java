package com.example.se7;

public class Exercise03 {
	public static void fun() throws E2 {
	}

	public static void gun() throws E2, E3, E4 {
	}

	public static void main(String[] args) throws E2, E3, E4 {
		try {
			fun(); // E2
		} catch (E1 e) {
			System.err.println(e.getMessage());
			throw e; // E2
		}
		
		try {
			gun();
		} catch (E2 | E3 | E4 e) { // multi-catch
			System.err.println(e.getMessage());
			throw e;
		}
	}
	
	
}

@SuppressWarnings("serial")
class E1 extends Exception {
}

@SuppressWarnings("serial")
class E2 extends E1 {
}

@SuppressWarnings("serial")
class E3 extends E1 {
}

@SuppressWarnings("serial")
class E4 extends E1 {
}
