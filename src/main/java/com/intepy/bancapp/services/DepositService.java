package com.intepy.bancapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intepy.bancapp.entities.Account;
import com.intepy.bancapp.entities.Deposit;
import com.intepy.bancapp.exceptions.EntityNotFoundException;
import com.intepy.bancapp.repositories.AccountRepository;
import com.intepy.bancapp.repositories.DepositRepository;

import jakarta.transaction.Transactional;

@Service
public class DepositService {

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private AccountRepository accountRepository;

    public List<Deposit> listDeposits() {
        return depositRepository.findAll();
    }

    public Optional<Deposit> getDepositById(Long id) {
        return depositRepository.findById(id);
    }

    @Transactional
    public Deposit saveDeposit(Deposit deposit) {
        // CORRECTION: Fetch the complete account from the database
        if (deposit.getAccount() == null || deposit.getAccount().getId() == null) {
            throw new EntityNotFoundException("Account is required");
        }

        Long accountId = deposit.getAccount().getId();
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + accountId));

        // Update the account balance
        account.setBalance(account.getBalance() + deposit.getAmount());
        accountRepository.save(account);

        // Associate the complete account to the deposit
        deposit.setAccount(account);

        return depositRepository.save(deposit);
    }

    @Transactional
    public Deposit updateDeposit(Long id, Deposit updatedDeposit) {
        return depositRepository.findById(id)
                .map(deposit -> {
                    // If the amount changed, adjust the account balance
                    if (!deposit.getAmount().equals(updatedDeposit.getAmount())) {
                        Account account = deposit.getAccount();
                        if (account != null) {
                            // Revert the previous deposit and apply the new one
                            double difference = updatedDeposit.getAmount() - deposit.getAmount();
                            account.setBalance(account.getBalance() + difference);
                            accountRepository.save(account);
                        }
                    }

                    deposit.setAmount(updatedDeposit.getAmount());

                    // CORRECTION: If changing the account, fetch the new complete account
                    if (updatedDeposit.getAccount() != null &&
                            updatedDeposit.getAccount().getId() != null) {
                        Long newAccountId = updatedDeposit.getAccount().getId();
                        Account newAccount = accountRepository.findById(newAccountId)
                                .orElseThrow(() -> new EntityNotFoundException(
                                        "Account not found with id: " + newAccountId));
                        deposit.setAccount(newAccount);
                    }

                    return depositRepository.save(deposit);
                })
                .orElseThrow(() -> new EntityNotFoundException("Deposit not found with id: " + id));
    }

    @Transactional
    public void deleteDeposit(Long id) {
        Optional<Deposit> depositOpt = depositRepository.findById(id);
        if (depositOpt.isPresent()) {
            Deposit deposit = depositOpt.get();
            // Revert the deposit from the account balance
            Account account = deposit.getAccount();
            if (account != null) {
                account.setBalance(account.getBalance() - deposit.getAmount());
                accountRepository.save(account);
            }
        }
        depositRepository.deleteById(id);
    }
}
