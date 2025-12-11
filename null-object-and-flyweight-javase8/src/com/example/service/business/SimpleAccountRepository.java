package com.example.service.business;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import com.example.domain.Account;
import com.example.service.AccountRepository;

public class SimpleAccountRepository implements AccountRepository {

	private Map<String, Account> accounts = new ConcurrentHashMap<>();

	@Override
	public Optional<Account> findByIban(String iban) {
		return Optional.ofNullable(accounts.get(iban));
	}

	@Override
	public Stream<Account> findAll(int pageNo, int pageSize) {
		return accounts.values().stream().skip(pageNo * pageSize).limit(pageSize);
	}

	@Override
	public Optional<Account> removeByIban(String iban) {
		return null;
	}

	@Override
	public void createAccount(Account account) {
		accounts.putIfAbsent(account.getIban(), account);
	}

	@Override
	public void updateAccount(Account account) {
		String iban = account.getIban();
		Account found = accounts.get(iban);
		if (Objects.nonNull(found)) {
			found.setBalance(account.getBalance());
		}
	}

}
