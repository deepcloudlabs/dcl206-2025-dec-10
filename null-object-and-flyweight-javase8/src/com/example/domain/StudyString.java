package com.example.domain;

public class StudyString {

	public static void main(String[] args) {
		String name1 = "jack"; // Object Pool
		String name2 = "jack"; // Object Pool
		String name3 = new String("jack"); // Heap
		name2.toUpperCase();
		name3 = name3.intern(); // Heap -> Object Pool
		System.out.println("name1 == name2 ? "+(name1==name2));
		System.out.println("name1 == name3 ? "+(name1==name3));
        // java se 8, GC1 -> automatic internalization, String De-duplication
		// -XX+UseStringDeduplication -XX+UseG1GC
		AAA[] array = new AAA[100]; // 16B
		array[0] = new AAA(42);
		array[1] = new AAA(108);
	}

}

record AAA(int data) {}
