package com.example.se12;

public class Exercise01 {
	public static void main(String[] args) {
		String day = "wednesday";
		var message =
		switch (day) {
			case "monday", "tuesday", "wednesday", "thurday", "friday" -> {
				yield "Work hard!";
			}
			case "saturday", "sunday" -> {
				yield "Have rest!";
			}
			default -> {
				throw new IllegalArgumentException("No such day is available: %s".formatted(day));
			}
		};
		System.out.println(message);
	}
}
