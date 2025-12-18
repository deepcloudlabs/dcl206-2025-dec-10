package com.example.se17;

public class Exercise01 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

// abstraction -> design
sealed abstract class Animal permits Spider, Cat, Fish {
	abstract public int getLegs();
}

interface Pet {
	default void play() {
	}
}

// Solution Class
final class Spider extends Animal {

	@Override
	public int getLegs() {
		return 8;
	}

}

sealed class Cat extends Animal implements Pet permits VanCat {

	@Override
	public int getLegs() {
		return 4;
	}
}

final class Fish extends Animal implements Pet {

	@Override
	public int getLegs() {
		return 0;
	}
}

non-sealed class VanCat extends Cat {
}