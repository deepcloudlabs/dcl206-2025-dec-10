package com.example.se10;

import java.util.List;

public class Exercise01 {
    
	public static void main(String[] args) {
		var x = 42; // auto
        List<Integer> numbers1 = List.of(4,8,15,16,23,42); // 
        var numbers2 = List.of(4,8,15,16.,23,42); // List<Integer>
        var numbers3 = List.of(Integer.valueOf(4),8,15,Double.valueOf(16.0),23,42); // List<Number & Comparable<?> & Constable & ConstantDesc>
        var numbers4 = List.of(4,8,"15",16.0,23,42); // List<List<Object & Serializable & Comparable<?> & Constable & ConstantDesc>>

	}

}
