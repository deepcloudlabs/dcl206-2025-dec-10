package com.example.se17;

import java.util.List;

public class Exercise02 {
	public static void main(String[] args) {
		var animals = List.of(
				new Spider(),new Cat(),new Fish(),
				new Spider(),new Cat(),new Fish()
		);
		for (var animal : animals) {
			if (animal instanceof Pet pet) {
				pet.play();
			}
		}
		for (var animal : animals) {
			switch (animal) {
				case null -> {
					System.out.println("No animal");
				}
				case Cat cat when (cat.getLegs() == 4)-> {
					System.out.println(cat.getLegs());
				}
				case Fish fish -> {
					System.out.println(fish.getLegs());					
				}
				default -> {
					System.out.println("No such animal!");
				}
			}
		}
	}
}
