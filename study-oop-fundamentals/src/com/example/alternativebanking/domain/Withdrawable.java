package com.example.alternativebanking.domain;

sealed public abstract interface Withdrawable permits DebitAccount {
	public abstract double withdraw(double amount);
}
