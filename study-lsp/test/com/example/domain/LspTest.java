package com.example.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.example.service.TestLspService;

class LspTest {

	@Test
	void testLsp_rectangle() {
		var service = new TestLspService();
		assertEquals(20.0, service.testLSP(new Rectangle(4,5)));
	}

	@Test
	void testLsp_square() {
		var service = new TestLspService();
		assertEquals(16.0, service.testLSP(new Square(4)));
	}
	
	@Test
	void testLsp_triangle() {
		var service = new TestLspService();
		assertEquals(6.0, service.testLSP(new Triangle(3,4,5)));
	}

}
