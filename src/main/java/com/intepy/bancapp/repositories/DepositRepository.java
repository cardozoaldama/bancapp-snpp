package com.intepy.bancapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intepy.bancapp.entities.Deposit;

public interface DepositRepository extends JpaRepository<Deposit, Long> {

}
