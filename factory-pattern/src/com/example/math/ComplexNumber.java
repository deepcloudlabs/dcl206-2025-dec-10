package com.example.math;

import static java.lang.Math.*;

/**
 *
 * @author Binnur Kurt <binnur.kurt@gmail.com>
 */
public class ComplexNumber {
	private double real, img;

	public ComplexNumber() {
		real = 1;
		img = 0;
	}

	public ComplexNumber(double real, double img) {
		this.real = real;
		this.img = img;
	}

	public double getImg() {
		return img;
	}

	public void setImg(double img) {
		this.img = img;
	}

	public double getReal() {
		return real;
	}

	public void setReal(double real) {
		this.real = real;
	}

	public static ComplexNumber fromPolar(double r, double ang) {
		return new ComplexNumber(r * cos(ang), r * sin(ang));
	}

	public static ComplexNumber fromCartesian(double a, double b) {
		return new ComplexNumber(a, b);
	}

	@Override
	public String toString() {
		return "[Real=" + real + ",img=" + img + "]";
	}

}
