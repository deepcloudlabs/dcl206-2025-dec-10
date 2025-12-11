package com.example.banking.domain;

non-sealed public class DebitAccount extends Account {

	public DebitAccount(String iban) {
		super(iban);
	}

	@Override
	public double deposit(double amount) {
		if (status != AccountStatus.ACTIVE)
			return this.balance;
		if (amount <= 0)
			return this.balance;
		this.balance += amount;
		return this.balance;
	}

	@Override
	public double withdraw(double amount) {
		if (status != AccountStatus.ACTIVE)
			return this.balance;
		if (amount <= 0)
			return this.balance;
		if (amount > this.balance)
			return this.balance;
		this.balance -= amount;
		return this.balance;
	}

}
