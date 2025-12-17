package com.example.se7;

import java.io.Closeable;

public class Exercise04 {
	public static int fun() {
		try {
			System.err.println("inside try!");
			throw new RuntimeException("Ooops");
		}catch (Exception e) {
			System.err.println("inside catch(Exception e)...");
			return 549;
		}finally {
			System.err.println("inside finally!");
			return 3615;
		}
	}
	public static void main(String[] args) {
		System.out.println(fun());
		try(
				var res1= new PreciousResource(1);
				var res2= new PreciousResource(2);
				var res3= new PreciousResource(3);				
		) {
			throw new IllegalArgumentException("Oooops");
		}catch (Exception e) {
			System.out.println(e.getMessage());
			for (var ex : e.getSuppressed()) {
				System.out.println(ex.getMessage());
			}
		}		
	}

}

class PreciousResource implements AutoCloseable {
	private final int id;
	
	public PreciousResource(int id) {
		this.id = id;
	}

	@Override
	public void close() throws Exception {
		System.out.println("Closing thre resource %d".formatted(id));
		throw new RuntimeException("Something is wrong while closing the resource %d".formatted(id));
	}
	
}