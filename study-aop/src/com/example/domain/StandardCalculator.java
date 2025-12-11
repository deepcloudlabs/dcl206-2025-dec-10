package com.example.domain;

@Cacheable
public class StandardCalculator implements Calculator {

	private Calculator self = this;
	
	public void setProxy(Calculator proxy) { this.self = proxy;}
	
	@Override
	public double add(double x, double y) {
		System.out.println("add(%f,%f) runs.".formatted(x, y));
		return x + y;
	}

	@Override
	public double sub(double x, double y) {
		System.out.println("sub(%f,%f) runs.".formatted(x, y));
		return x - y;
	}

	@Override
	public double mul(double x, double y) {
		var sum = 0.0;
		for (var i = 0; i < x; ++i)
			sum = self.add(sum,y);
		System.out.println("mul(%f,%f) runs.".formatted(x, y));
		return sum;
	}

	@Override
	public double div(double x, double y) {
		System.out.println("div(%f,%f) runs.".formatted(x, y));
		return x / y;
	}

}
