package com.example.se15;

public class Exercise01 {

	public static void main(String[] args) {
		Point p1 = new Point(-1, 2);
		Point p2 = new Point(1, 2);
		System.out.println(p1.toString());
		System.out.println(p2.toString());
		System.out.println(p1.equals(p2));
		System.out.println(p1.hashCode());
		System.out.println(p2.hashCode());
		System.out.println(p1.x());
		System.out.println(p1.y());
	}

}


record Point(int x,int y) {
	public Point {
		if (x<0 || y<0) throw new IllegalArgumentException("Cannot be negative");		
	}
	
}

