package com.intepy.bancapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intepy.bancapp.entities.Loan;
import com.intepy.bancapp.entities.User;
import com.intepy.bancapp.exceptions.EntityNotFoundException;
import com.intepy.bancapp.repositories.LoanRepository;
import com.intepy.bancapp.repositories.UserRepository;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Loan> listLoans() {
        return loanRepository.findAll();
    }

    public Optional<Loan> getLoanById(Long id) {
        return loanRepository.findById(id);
    }

    public Loan saveLoan(Loan loan) {
        // CORRECTION: If the loan has a user, fetch the complete user
        if (loan.getUser() != null && loan.getUser().getId() != null) {
            Long userId = loan.getUser().getId();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "User not found with id: " + userId));
            loan.setUser(user);
        }

        return loanRepository.save(loan);
    }

    public Loan updateLoan(Long id, Loan updatedLoan) {
        return loanRepository.findById(id)
                .map(loan -> {
                    loan.setAmount(updatedLoan.getAmount());
                    loan.setStatus(updatedLoan.getStatus());

                    // CORRECTION: If updating the user, fetch the complete user
                    if (updatedLoan.getUser() != null &&
                            updatedLoan.getUser().getId() != null) {
                        Long userId = updatedLoan.getUser().getId();
                        User user = userRepository.findById(userId)
                                .orElseThrow(() -> new EntityNotFoundException(
                                        "User not found with id: " + userId));
                        loan.setUser(user);
                    }

                    return loanRepository.save(loan);
                })
                .orElseThrow(() -> new EntityNotFoundException("Loan not found with id: " + id));
    }

    public void deleteLoan(Long id) {
        loanRepository.deleteById(id);
    }
}
