package com.example.alternativebanking.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Customer {
	// information hiding
	private final String identity;
	private List<Account> accounts;

	public Customer(String identity) {
		this.identity = identity;
		accounts = new ArrayList<>();
	}

	public List<Account> getAccounts() {
		return Collections.unmodifiableList(accounts); // applies information hiding principle
		// return accounts; // violates information hiding principle
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

	public String getIdentity() {
		return identity;
	}

	public void addAccount(Account account) {
		Objects.requireNonNull(account);
		this.accounts.add(account);
	}

	
}
