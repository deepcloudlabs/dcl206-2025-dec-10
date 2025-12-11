package com.example.alternativebanking.domain;

import com.example.banking.domain.AccountStatus;

public final class SavingsAccount implements Account, Depositable {
	private final String iban;
	protected double balance;
	protected AccountStatus status;

	public SavingsAccount(String iban) {
		this(iban, 0.0);
	}

	public SavingsAccount(String iban, double balance) {
		this.iban = iban;
		this.balance = balance;
		this.status = AccountStatus.ACTIVE;
	}

	@Override
	public String getIban() {
		return iban;
	}

	@Override
	public double getBalance() {
		return balance;
	}

	@Override
	public AccountStatus getStatus() {
		return status;
	}

	@Override
	public void setStatus(AccountStatus status) {
		this.status = status;
	}

	@Override
	public double deposit(double amount) {
		System.err.println("SavingsAccount::deposit");
		if (status != AccountStatus.ACTIVE)
			return this.balance;
		if (amount <= 0)
			return this.balance;
		this.balance += amount;
		return this.balance;
	}

}
