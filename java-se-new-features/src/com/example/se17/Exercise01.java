package com.example.se17;

public class Exercise01 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

// abstraction -> design
sealed abstract class Animal permits Spider, Cat, Fish{}
interface Pet {}

// Solution Class
final class Spider extends Animal {}
sealed class Cat extends Animal implements Pet permits VanCat {}
final class Fish extends Animal implements Pet {}
non-sealed class VanCat extends Cat {}