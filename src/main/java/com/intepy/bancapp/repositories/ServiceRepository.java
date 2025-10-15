package com.intepy.bancapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intepy.bancapp.entities.Service;

public interface ServiceRepository extends JpaRepository<Service, Long> {

}
