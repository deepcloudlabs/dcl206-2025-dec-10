package com.example.banking.domain;

sealed public abstract class Account permits DebitAccount, SavingsAccount, CheckingAccount{
	private final String iban;
	protected double balance;
	protected AccountStatus status;

	public Account(String iban) {
		this.iban = iban;
		balance = 0.0;
		status = AccountStatus.ACTIVE;
	}

	public double getBalance() {
		return balance;
	}

	public AccountStatus getStatus() {
		return status;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
	}

	public String getIban() {
		return iban;
	}

	public abstract double deposit(double amount);

	public abstract double withdraw(double amount);

}
