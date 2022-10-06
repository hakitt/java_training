package com.example.java_proj;

import java.util.List;

public interface AccountService {

    Account addAccount(Account account);

    List<Account> fetchAccountList();

    Account updateAccount(Account account);
}
