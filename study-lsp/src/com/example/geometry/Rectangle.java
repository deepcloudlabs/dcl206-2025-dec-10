package com.example.geometry;

public record Rectangle(double x, double y, double width, double height) implements Shape2D {

	@Override
	public double area() {
		return width * height;
	}

	@Override
	public double circumference() {
		return 2.0 * (width + height);
	}

}
