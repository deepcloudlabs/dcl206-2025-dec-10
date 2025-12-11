package com.example.application;

import java.lang.reflect.Proxy;

import com.example.domain.Calculator;
import com.example.domain.StandardCalculator;
import com.example.handler.CachingHandler;

public class CalculatorApplication {

	public static void main(String[] args) {
		var standardCalculator = new StandardCalculator();
		var clazz = standardCalculator.getClass();
		var calculator = (Calculator) Proxy.newProxyInstance(
				clazz.getClassLoader(), 
				clazz.getInterfaces(), 
				new CachingHandler(standardCalculator)
		);
		standardCalculator.setProxy(calculator);
		System.err.println(calculator.getClass().getName());
		System.out.println("3*5: %f".formatted(calculator.mul(3, 5)));
		System.out.println("3*5: %f".formatted(calculator.mul(3, 5)));
		System.out.println("3*5: %f".formatted(calculator.mul(3, 5)));
		System.out.println("3*5: %f".formatted(calculator.mul(3, 5)));
		System.out.println("3*5: %f".formatted(calculator.mul(3, 5)));
	}

}
