package com.example.service;
// Fat Interface violates ISP
public interface Calculator {
	double add(double x, double y);
	double subtract(double x, double y);
	double multiply(double x, double y);
	double divide(double x, double y);
	double inverse(double x);
	double squareRoot(double x, double y);
	double square(double x, double y);
	
	double kilogramToNewton(double value);
	double kilometerToMile(double value);
	
	int factorial(int n);
	int mod(int m,int n);
	int abs(int n);
	
	int floor(double x);
	int ciel(double x);
	int around(double x);
	
	double sin(double x);
	double cos(double x);
	double tan(double x);
	
	double log(double x);
	double ln(double x);
}
