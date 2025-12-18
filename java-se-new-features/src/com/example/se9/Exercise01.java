package com.example.se9;

public class Exercise01 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

interface III {
	default void fun() {
		sun();
	}
	default void gun() {
		sun();
	}
	static void pun() {
		common();
	}
	static void run() {
		common();		
	}
	private static void common() {} // java se 9+
	private void sun() {} // java se 9+
}