package com.example.se7;

public class Exercise02 {

	public static void main(String[] args) {
		String day = "wednesday";
		switch(day) {
		case "monday":
		case "tuesday":
		case "wednesday":
		case "thurday":
		case "friday":
			System.out.println("Work hard!");
			break;
		case "saturday":	
		case "sunday":
			System.out.println("Have rest!");
			break;
			default:
				throw new IllegalArgumentException("No such day is available: %s".formatted(day));
		}

	}

}
