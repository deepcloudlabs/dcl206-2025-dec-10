package com.example.se8;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.ToLongFunction;
import java.util.stream.IntStream;

public class Exercise02 {
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		List<Integer> numbers = List.of(4,8,15,16,23,42);
		// Predicate<Integer> ifOdd = t -> t%2 == 1;
		Predicate<Integer> ifOdd = NumberUtility::isOdd;
		ToLongFunction<Integer> toLongCube = u -> u*u*u;
		long total = numbers.stream()
					       //.filter(ifOdd)
					       .filter(NumberUtility::isOdd)
					       .mapToLong(NumberUtility::toCube)
					       .sum();
		System.out.println(total);
		//Consumer<Integer> printNumber = n -> System.out.println(n);
		Consumer<Integer> printNumber = System.out::println;
		IntStream.range(0, 8)
		         .mapToObj( _ -> ThreadLocalRandom.current().nextInt(1,80))
		         .distinct()
		         .sorted()
		         .forEach(printNumber);
	}
}

interface NumberUtility {
	public static boolean isOdd(int n) {
		return n%2 == 1;
	}
	static long toCube(int n) {
		return n*n*n;
	}
}