package com.intepy.bancapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intepy.bancapp.entities.Account;
import com.intepy.bancapp.entities.ServicePayment;
import com.intepy.bancapp.exceptions.EntityNotFoundException;
import com.intepy.bancapp.exceptions.InsufficientBalanceException;
import com.intepy.bancapp.exceptions.ValidationException;
import com.intepy.bancapp.repositories.AccountRepository;
import com.intepy.bancapp.repositories.ServicePaymentRepository;
import com.intepy.bancapp.repositories.ServiceRepository;

@Service
public class ServicePaymentService {

    @Autowired
    private ServicePaymentRepository servicePaymentRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    public List<ServicePayment> listPayments() {
        return servicePaymentRepository.findAll();
    }

    public Optional<ServicePayment> getPaymentById(Long id) {
        return servicePaymentRepository.findById(id);
    }

    @Transactional
    public ServicePayment savePayment(ServicePayment payment) {
        // CORRECTION: Validate and fetch the complete account
        if (payment.getAccount() == null || payment.getAccount().getId() == null) {
            throw new ValidationException("Account is required to make the payment");
        }

        // CORRECTION: Validate and fetch the complete service
        if (payment.getService() == null || payment.getService().getId() == null) {
            throw new ValidationException("Service is required to make the payment");
        }

        Long accountId = payment.getAccount().getId();
        Long serviceId = payment.getService().getId();

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + accountId));

        com.intepy.bancapp.entities.Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("Service not found with id: " + serviceId));

        Double amount = payment.getAmount();

        // Validate that the account has sufficient balance
        if (account.getBalance() < amount) {
            throw new InsufficientBalanceException(
                    "Insufficient balance to make the service payment. Current balance: " + account.getBalance());
        }

        // Deduct the amount from the account
        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);

        // Associate the complete entities to the payment
        payment.setAccount(account);
        payment.setService(service);

        // Save the payment
        return servicePaymentRepository.save(payment);
    }

    @Transactional
    public ServicePayment updatePayment(Long id, ServicePayment updatedPayment) {
        return servicePaymentRepository.findById(id)
                .map(payment -> {
                    // Revert the previous payment
                    Account previousAccount = payment.getAccount();
                    Double previousAmount = payment.getAmount();

                    if (previousAccount != null) {
                        previousAccount.setBalance(previousAccount.getBalance() + previousAmount);
                        accountRepository.save(previousAccount);
                    }

                    // CORRECTION: Fetch the new complete account
                    Long newAccountId = updatedPayment.getAccount().getId();
                    Account newAccount = accountRepository.findById(newAccountId)
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "Account not found with id: " + newAccountId));

                    // CORRECTION: Fetch the new complete service
                    Long newServiceId = updatedPayment.getService().getId();
                    com.intepy.bancapp.entities.Service newService = serviceRepository.findById(newServiceId)
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "Service not found with id: " + newServiceId));

                    Double newAmount = updatedPayment.getAmount();

                    // Validate sufficient balance
                    if (newAccount.getBalance() < newAmount) {
                        throw new InsufficientBalanceException("Insufficient balance for the new payment amount");
                    }

                    newAccount.setBalance(newAccount.getBalance() - newAmount);
                    accountRepository.save(newAccount);

                    // Update the payment
                    payment.setAmount(newAmount);
                    payment.setAccount(newAccount);
                    payment.setService(newService);

                    return servicePaymentRepository.save(payment);
                })
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + id));
    }

    @Transactional
    public void deletePayment(Long id) {
        Optional<ServicePayment> paymentOpt = servicePaymentRepository.findById(id);
        if (paymentOpt.isPresent()) {
            ServicePayment payment = paymentOpt.get();

            // Revert the payment - return the money to the account
            Account account = payment.getAccount();
            if (account != null) {
                account.setBalance(account.getBalance() + payment.getAmount());
                accountRepository.save(account);
            }
        }

        servicePaymentRepository.deleteById(id);
    }
}
