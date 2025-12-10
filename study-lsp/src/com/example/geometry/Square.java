package com.example.geometry;

public record Square(double x,double y,double edge) implements Shape2D {

	@Override
	public double area() {
		return edge * edge;
	}

	@Override
	public double circumference() {
		return 4.0 * edge;
	}

}
