package com.example.alternativebanking.domain;

import com.example.banking.domain.AccountStatus;

public interface Account {
	public abstract String getIban();
	public abstract double getBalance();
	public abstract AccountStatus getStatus();
	public abstract void setStatus(AccountStatus status);
}
