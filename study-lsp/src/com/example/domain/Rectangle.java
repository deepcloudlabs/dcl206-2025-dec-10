package com.example.domain;

public class Rectangle implements Shape {
	private double width;
	private double height;

	public Rectangle(double w, double h) {
		width = w;
		height = h;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public void setWidth(double w) {
		width = w;
	}

	public void setHeight(double h) {
		height = h;
	}

	@Override
	public double area() {
		return (width * height);
	}

}
