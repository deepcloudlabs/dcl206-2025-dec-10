package com.example.se14;

public class Exercise02 {
	public static void main(String[] args) {
		Animal animal = new Cat();
		
		if (animal instanceof Pet pet) { // Guard
			// var pet = (Pet) animal;
			pet.play();
		}		
	}
}

//abstraction -> design
sealed abstract class Animal permits Spider, Cat, Fish {
}

interface Pet {
	default void play() {
		System.out.println("Playing with the pet...");
	}
}

//Solution Class
final class Spider extends Animal {
}

sealed class Cat extends Animal implements Pet permits VanCat {
}

final class Fish extends Animal implements Pet {
}

non-sealed class VanCat extends Cat {
}