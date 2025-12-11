package com.example;

public class Exercise01 {

	public static void main(String[] args) throws InterruptedException {
		Thread t1 = new Thread(() ->{
			System.out.println("Hello Mars!");
		});
		var task1 = new Task1();
		Thread t2 = new Thread(task1);
		t1.start();
		t2.start();
		System.out.println("Application is closing...");
		t1.join();
		t2.join();
		System.out.println(task1.getData());
		System.out.println("Application is closed.");
	}

}

class Task1 implements Runnable {
	private int data;
	@Override
	public void run() {
		System.out.println("Hello Moon!");
		data=42;
	}
	public int getData() {
		return data;
	}
	
}