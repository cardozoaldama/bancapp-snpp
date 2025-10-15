package com.intepy.bancapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intepy.bancapp.entities.Account;
import com.intepy.bancapp.entities.User;
import com.intepy.bancapp.exceptions.EntityNotFoundException;
import com.intepy.bancapp.repositories.AccountRepository;
import com.intepy.bancapp.repositories.UserRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Account> listAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    public Account saveAccount(Account account) {
        // CORRECTION: If the account has a user, fetch the complete user
        if (account.getUser() != null && account.getUser().getId() != null) {
            Long userId = account.getUser().getId();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "User not found with id: " + userId));
            account.setUser(user);
        }

        return accountRepository.save(account);
    }

    public Account updateAccount(Long id, Account updatedAccount) {
        return accountRepository.findById(id)
                .map(account -> {
                    account.setNumber(updatedAccount.getNumber());
                    account.setBalance(updatedAccount.getBalance());
                    if (updatedAccount.getAccountType() != null) {
                        account.setAccountType(updatedAccount.getAccountType());
                    }

                    // CORRECTION: If updating the user, fetch the complete user
                    if (updatedAccount.getUser() != null &&
                            updatedAccount.getUser().getId() != null) {
                        Long userId = updatedAccount.getUser().getId();
                        User user = userRepository.findById(userId)
                                .orElseThrow(() -> new EntityNotFoundException(
                                        "User not found with id: " + userId));
                        account.setUser(user);
                    }

                    return accountRepository.save(account);
                })
                .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + id));
    }

    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }
}
