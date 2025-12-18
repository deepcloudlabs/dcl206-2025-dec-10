package com.example.se19;

public class Exercise01 {

	public static void main(String[] args) {
		Object o = new Point2D(5,7);
		// code block
		if (o instanceof Point2D(int x,int y)) {
			System.out.println("%d,%d".formatted(x,y));
		}
		switch(o) {
		case Point2D(int x,int y) when (x < y) -> {
			System.out.println("%d,%d".formatted(x,y));
		} 
		default -> {
			
		}
		}
	}

}

record Point2D(int x,int y) {}