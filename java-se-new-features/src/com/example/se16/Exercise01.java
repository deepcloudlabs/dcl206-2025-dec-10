package com.example.se16;

public class Exercise01 {
public static void main(String[] args) {
	
}
}

record Point(int x,int y) {
	public Point {
		if (x<0 || y<0) throw new IllegalArgumentException("Cannot be negative");		
	}
	
}

@SuppressWarnings("unused")
class A {
	private Point p;
	class B { // inner class
		private Point p = new Point(0,0);	
	}
}