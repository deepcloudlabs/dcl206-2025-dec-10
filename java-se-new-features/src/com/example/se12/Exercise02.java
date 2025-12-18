package com.example.se12;

public class Exercise02 {
	public static void main(String[] args) {
		String day = "wednesdayy";
		var message =
		switch (day) {
			case "monday", "tuesday", "wednesday", "thurday", "friday" -> "Work hard!";
			case "saturday", "sunday" -> "Have rest!";
			default -> "No such day is available: %s".formatted(day);
		};
		System.out.println(message);
	}
}
