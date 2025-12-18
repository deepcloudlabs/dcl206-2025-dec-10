package com.example.se16;

import jdk.incubator.vector.IntVector;

public class Exercise02 {
	public static void main(String[] args) {
		int[] u = { 1, 2, 3, 4 };
		int[] v = { 10, 20, 30, 40 };
		var w = new int[u.length];
		for (var i = 0; i < u.length; ++i) {
			w[i] = u[i] * v[i];	
		}
		var uVector = IntVector.fromArray(IntVector.SPECIES_128, u, 0);
		var vVector = IntVector.fromArray(IntVector.SPECIES_128, v, 0);
		var wVector = uVector.mul(vVector);
		wVector.intoArray(w, 0);
	}
}
