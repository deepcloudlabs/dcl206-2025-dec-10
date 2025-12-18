package com.example.se25;

public class Exercise04 {

}

class Circle {
	private int x,y;
	private int radius;
	
	public Circle(int x, int y, int radius) {
		this.x = x;
		this.y = y;
		this.radius = radius;
	}

	@Override
	public String toString() {
		return "Circle [x=" + x + ", y=" + y + ", radius=" + radius + "]";
	}
		
}

class ColorfulCircle extends Circle {
	private String color;

	public ColorfulCircle(int x, int y, int radius, String color) {
	    if (radius <= 0) throw new IllegalArgumentException("radius should be positive");
		super(x, y, radius);
		this.color = color;
	}
	
}