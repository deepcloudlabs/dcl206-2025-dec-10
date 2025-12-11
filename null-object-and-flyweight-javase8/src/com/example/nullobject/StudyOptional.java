package com.example.nullobject;

import java.util.function.Consumer;

import com.example.domain.Account;
import com.example.service.AccountRepository;
import com.example.service.business.SimpleAccountRepository;

public class StudyOptional {

	public static void main(String[] args) {
		AccountRepository repo = new SimpleAccountRepository();
		repo.createAccount(new Account("TR1", 100_000));
		Consumer<Account> printBalance = acc -> System.out.println("Balance: " + acc.getBalance());
		repo.findByIban("TR1").ifPresent(printBalance);
		repo.findByIban("TR1").orElseThrow(() -> new IllegalArgumentException("Account is not found"));
		repo.findByIban("TR1").orElse(new Account("TR1",20_000));
		repo.findAll(0, 10000000); // zero cost
		repo.findAll(0, 5).forEach(printBalance);
	}
}
