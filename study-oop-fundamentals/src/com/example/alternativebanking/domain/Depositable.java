package com.example.alternativebanking.domain;

sealed public abstract interface Depositable permits DebitAccount, SavingsAccount {
	public abstract double deposit(double amount);
}
