package com.intepy.bancapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intepy.bancapp.entities.Deposito;

public interface DepositoRepository extends JpaRepository<Deposito, Long> {

}
