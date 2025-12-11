package com.example.domain;

import java.util.List;

public class StudyInteger {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		// -Djava.lang.Integer.IntegerCache.high=2048
		// wrapper class
		Integer i= 42; // i -> reference: 8-Byte ---> 12-Byte + 4-Byte = 24-Byte
		int j= 42; // 4-Byte
		List<Integer> numbers ;
		Integer x = Integer.valueOf(549);
		Integer y = 549;
		Integer u = 108;
		Integer v = 108;
		System.out.println("x==y? "+(x==y));
		System.out.println("u==v? "+(u==v));
	}

}
