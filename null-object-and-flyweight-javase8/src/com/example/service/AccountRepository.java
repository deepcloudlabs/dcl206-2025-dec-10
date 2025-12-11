package com.example.service;

import java.util.Optional;
import java.util.stream.Stream;

import com.example.domain.Account;

public interface AccountRepository {
	Optional<Account> findByIban(String iban);

	Stream<Account> findAll(int pageNo, int pageSize);

	Optional<Account> removeByIban(String iban);

	void createAccount(Account account);

	void updateAccount(Account account);

}
