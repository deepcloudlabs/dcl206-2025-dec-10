package com.example.alternativebanking.domain;

import com.example.banking.domain.AccountStatus;

public final class DebitAccount implements Account, Depositable, Withdrawable {
	private final String iban;
	protected double balance;
	protected AccountStatus status;

	public DebitAccount(String iban) {
		this.iban = iban;
		this.status = AccountStatus.ACTIVE;
		this.balance = 0.0;
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
		System.err.println("DebitAccount::deposit");
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
