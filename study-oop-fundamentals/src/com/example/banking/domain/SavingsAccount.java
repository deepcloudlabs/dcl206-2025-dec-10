package com.example.banking.domain;

final public class SavingsAccount extends Account {

	public SavingsAccount(String iban) {
		super(iban);
	}

	@Override
	public double deposit(double amount) {
		throw new IllegalStateException("Cannot deposit from Savings Account");
	}

	@Override
	public double withdraw(double amount) {
		throw new IllegalStateException("Cannot withdraw from Savings Account");
	}

}
