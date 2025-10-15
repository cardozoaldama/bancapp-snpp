package com.intepy.bancapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intepy.bancapp.entities.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long> {

}
