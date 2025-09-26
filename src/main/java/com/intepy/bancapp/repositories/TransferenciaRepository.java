package com.intepy.bancapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intepy.bancapp.entities.Transferencia;

public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {

}
