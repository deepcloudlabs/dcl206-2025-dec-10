package com.example.se25;

public class Exercise05 {

	public static void main(String[] args) {
		System.out.println(fun(42));
		System.out.println(fun(-42));
		System.out.println(fun(0));
		System.out.println(fun(42.0));
		Class<?> type = int[].class;	
	}

	public static String fun(Number num) {
		return switch (num) {
			case int x when (x == 0) -> "zero";
			case int x when (x > 0) -> "positive integer";
			case int x when (x < 0) -> "negative integer";
		    default -> "other number types";
		};
	}
}
