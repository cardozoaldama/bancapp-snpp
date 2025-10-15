package com.intepy.bancapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intepy.bancapp.entities.ServicePayment;

public interface ServicePaymentRepository extends JpaRepository<ServicePayment, Long> {

}
