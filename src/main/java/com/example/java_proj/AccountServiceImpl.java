package com.example.java_proj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService{

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account addAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public List<Account> fetchAccountList() {
        return (List<Account>)accountRepository.findAll();
    }

    @Override
    public Account updateAccount(Account account) {
        Account accountDB = accountRepository.findById(account.getAccountNumber()).get();
        accountDB.setToken(account.getToken());
        return accountRepository.save(accountDB);
    }
}
