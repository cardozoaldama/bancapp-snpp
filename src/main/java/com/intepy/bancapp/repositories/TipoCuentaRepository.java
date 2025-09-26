package com.intepy.bancapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intepy.bancapp.entities.TipoCuenta;

public interface TipoCuentaRepository extends JpaRepository<TipoCuenta, Long> {

}
