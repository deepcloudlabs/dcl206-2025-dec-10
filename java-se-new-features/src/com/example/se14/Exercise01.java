package com.example.se14;

import java.util.Objects;

public class Exercise01 {

	public static void main(String[] args) {
		Point p1 = new Point(1, 2);
		Point p2 = new Point(1, 2);
		System.out.println(p1.toString());
		System.out.println(p2.toString());
		System.out.println(p1.equals(p2));
		System.out.println(p1.hashCode());
		System.out.println(p2.hashCode());
		System.out.println(p1.x());
		System.out.println(p1.y());
	}

}
interface I {
	default int fun() {return 42;}
}
interface J {
	int gun();
}

final record Point(int x,int y) implements I, J {
	Point(){
		this(0,0);	
	}
	
	Point(int x, int y){
		if (x<0 && y<0) throw new IllegalArgumentException("Cannot be negative");
		this.x = x;
		this.y = y;
	}
	
	@Override
	public int gun() {
		return 549;
	}
}

final class Point2D {
	private final int x;
	private final int y;
	public Point2D(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point2D other = (Point2D) obj;
		return x == other.x && y == other.y;
	}
	@Override
	public String toString() {
		return "Point2D [x=" + x + ", y=" + y + "]";
	}
	
}