package com.intepy.bancapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intepy.bancapp.entities.EstadoPrestamo;

public interface EstadoPrestamoRepository extends JpaRepository<EstadoPrestamo, Long> {

}
