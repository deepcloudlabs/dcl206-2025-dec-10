package com.example.shopping.domain;

/**
 *
 * @author Binnur Kurt <binnur.kurt@gmail.com>
 */
public class Sugar implements Coffee {
	private Coffee coffee;

	public Sugar(Coffee coffee) {
		this.coffee = coffee;
	}

	public double cost() {
		return 3 + coffee.cost();
	}

}
