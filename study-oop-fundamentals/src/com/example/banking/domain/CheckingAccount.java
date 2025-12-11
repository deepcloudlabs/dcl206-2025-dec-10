package com.example.banking.domain;

non-sealed public class CheckingAccount extends Account {
	private double overdraftAmount;

	public CheckingAccount(String iban) {
		super(iban);
	}

	public CheckingAccount(String iban, double overdraftAmount) {
		super(iban);
		this.overdraftAmount = overdraftAmount;
	}

	public double getOverdraftAmount() {
		return overdraftAmount;
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
		if (amount > (this.balance + this.overdraftAmount))
			return this.balance;
		this.balance -= amount;
		return this.balance;
	}

}
