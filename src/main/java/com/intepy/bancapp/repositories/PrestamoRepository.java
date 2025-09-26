package com.intepy.bancapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intepy.bancapp.entities.Prestamo;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

}
