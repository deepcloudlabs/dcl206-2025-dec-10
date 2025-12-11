package com.example.banking.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CustomerTest {

	@Test
	void test() {
		var jack = new Customer("11111111110");
		var clazz = jack.getAccounts().getClass();
		System.err.println(clazz.getName());
		jack.getAccounts().add(new DebitAccount("TR1"));
		assertEquals(1,jack.getAccounts().size());
	}

}
