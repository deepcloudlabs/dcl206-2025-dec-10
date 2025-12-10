package com.example.geometry;

public record Cube(double x,double y,double z,double edge) implements Shape2D, Shape3D {

	@Override
	public double volume() {
		return edge * edge * edge;
	}

	@Override
	public double area() {
		return 6 * edge * edge;
	}

	@Override
	public double circumference() {
		return 12.0 * edge;
	}

}
