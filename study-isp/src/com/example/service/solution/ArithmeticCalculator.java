package com.example.service.solution;

public interface ArithmeticCalculator {
	public static final int data= 42;
	
	public abstract double add(double x, double y);

	double subtract(double x, double y);

	double multiply(double x, double y);

	double divide(double x, double y);

	double inverse(double x);

	double squareRoot(double x, double y);

	double square(double x, double y);
}
