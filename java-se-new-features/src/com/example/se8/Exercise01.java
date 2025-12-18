package com.example.se8;

public class Exercise01 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

abstract interface III {
	static final int x = 42;
}

abstract interface JJJ { // since java se 8
	public default int fun() {
		return 42;
	}

	// I. Method Reference -> a. object's method b. static method -> interface ->
	// utility function II. Lambda Expression
	public static int run() { // functional programming -> HoF -> accepts function as parameter
		return 549;
	}
}

interface KKK {
	public abstract int gun();
}

interface MMM extends JJJ, KKK {
}

abstract class AAA implements III, JJJ, KKK {
}

class BBB extends AAA {

	@Override
	public int gun() {
		return 3615;
	}
}
