package com.intepy.bancapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intepy.bancapp.dto.TransferRequestDTO;
import com.intepy.bancapp.entities.Account;
import com.intepy.bancapp.entities.Transfer;
import com.intepy.bancapp.exceptions.EntityNotFoundException;
import com.intepy.bancapp.exceptions.InsufficientBalanceException;
import com.intepy.bancapp.exceptions.InvalidTransferException;
import com.intepy.bancapp.repositories.AccountRepository;
import com.intepy.bancapp.repositories.TransferRepository;

@Service
public class TransferService {

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private AccountRepository accountRepository;

    public List<Transfer> listTransfers() {
        return transferRepository.findAll();
    }

    public Optional<Transfer> getTransferById(Long id) {
        return transferRepository.findById(id);
    }

    @Transactional
    public Transfer createTransferFromDTO(TransferRequestDTO dto) {
        // Find accounts by account number
        Account sourceAccount = accountRepository.findByNumber(dto.getSourceAccountNumber())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Source account not found with number: " + dto.getSourceAccountNumber()));

        Account destinationAccount = accountRepository.findByNumber(dto.getDestinationAccountNumber())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Destination account not found with number: " + dto.getDestinationAccountNumber()));

        Double amount = dto.getAmount();

        // Validate that the source account has sufficient balance
        if (sourceAccount.getBalance() < amount) {
            throw new InsufficientBalanceException(
                    "Insufficient balance in source account. Current balance: " + sourceAccount.getBalance());
        }

        // Validate that it's not the same account
        if (sourceAccount.getId().equals(destinationAccount.getId())) {
            throw new InvalidTransferException("Source and destination account cannot be the same");
        }

        // Perform the transfer
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);

        // Save the updated accounts
        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        // Create and save the transfer
        Transfer transfer = new Transfer(amount, sourceAccount, destinationAccount);
        return transferRepository.save(transfer);
    }

    @Transactional
    public Transfer saveTransfer(Transfer transfer) {
        // CORRECTION: Fetch the complete accounts from the database
        if (transfer.getSourceAccount() == null || transfer.getSourceAccount().getId() == null) {
            throw new InvalidTransferException("Source account is required");
        }

        if (transfer.getDestinationAccount() == null || transfer.getDestinationAccount().getId() == null) {
            throw new InvalidTransferException("Destination account is required");
        }

        Long sourceAccountId = transfer.getSourceAccount().getId();
        Long destinationAccountId = transfer.getDestinationAccount().getId();

        Account sourceAccount = accountRepository.findById(sourceAccountId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Source account not found with id: " + sourceAccountId));

        Account destinationAccount = accountRepository.findById(destinationAccountId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Destination account not found with id: " + destinationAccountId));

        Double amount = transfer.getAmount();

        // Validate that the source account has sufficient balance
        if (sourceAccount.getBalance() < amount) {
            throw new InsufficientBalanceException(
                    "Insufficient balance in source account. Current balance: " + sourceAccount.getBalance());
        }

        // Validate that it's not the same account
        if (sourceAccount.getId().equals(destinationAccount.getId())) {
            throw new InvalidTransferException("Source and destination account cannot be the same");
        }

        // Perform the transfer
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);

        // Save the updated accounts
        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        // Associate the complete accounts to the transfer
        transfer.setSourceAccount(sourceAccount);
        transfer.setDestinationAccount(destinationAccount);

        // Save the transfer
        return transferRepository.save(transfer);
    }

    @Transactional
    public Transfer updateTransfer(Long id, Transfer updatedTransfer) {
        return transferRepository.findById(id)
                .map(transfer -> {
                    // Revert the previous transfer
                    Account previousSourceAccount = transfer.getSourceAccount();
                    Account previousDestinationAccount = transfer.getDestinationAccount();
                    Double previousAmount = transfer.getAmount();

                    if (previousSourceAccount != null && previousDestinationAccount != null) {
                        previousSourceAccount.setBalance(previousSourceAccount.getBalance() + previousAmount);
                        previousDestinationAccount.setBalance(previousDestinationAccount.getBalance() - previousAmount);
                        accountRepository.save(previousSourceAccount);
                        accountRepository.save(previousDestinationAccount);
                    }

                    // CORRECTION: Fetch the new complete accounts
                    Long newSourceAccountId = updatedTransfer.getSourceAccount().getId();
                    Long newDestinationAccountId = updatedTransfer.getDestinationAccount().getId();

                    Account newSourceAccount = accountRepository.findById(newSourceAccountId)
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "Source account not found with id: " + newSourceAccountId));

                    Account newDestinationAccount = accountRepository.findById(newDestinationAccountId)
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "Destination account not found with id: " + newDestinationAccountId));

                    Double newAmount = updatedTransfer.getAmount();

                    // Validate sufficient balance
                    if (newSourceAccount.getBalance() < newAmount) {
                        throw new InsufficientBalanceException("Insufficient balance in source account");
                    }

                    newSourceAccount.setBalance(newSourceAccount.getBalance() - newAmount);
                    newDestinationAccount.setBalance(newDestinationAccount.getBalance() + newAmount);

                    accountRepository.save(newSourceAccount);
                    accountRepository.save(newDestinationAccount);

                    // Update the transfer
                    transfer.setAmount(newAmount);
                    transfer.setSourceAccount(newSourceAccount);
                    transfer.setDestinationAccount(newDestinationAccount);

                    return transferRepository.save(transfer);
                })
                .orElseThrow(() -> new EntityNotFoundException("Transfer not found with id: " + id));
    }

    @Transactional
    public void deleteTransfer(Long id) {
        Optional<Transfer> transferOpt = transferRepository.findById(id);
        if (transferOpt.isPresent()) {
            Transfer transfer = transferOpt.get();

            // Revert the transfer
            Account sourceAccount = transfer.getSourceAccount();
            Account destinationAccount = transfer.getDestinationAccount();
            Double amount = transfer.getAmount();

            if (sourceAccount != null && destinationAccount != null) {
                sourceAccount.setBalance(sourceAccount.getBalance() + amount);
                destinationAccount.setBalance(destinationAccount.getBalance() - amount);

                accountRepository.save(sourceAccount);
                accountRepository.save(destinationAccount);
            }
        }

        transferRepository.deleteById(id);
    }
}
