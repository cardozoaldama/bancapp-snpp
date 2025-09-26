package com.intepy.bancapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intepy.bancapp.entities.Cuenta;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

}
