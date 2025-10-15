package com.intepy.bancapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intepy.bancapp.entities.Transfer;

public interface TransferRepository extends JpaRepository<Transfer, Long> {

}
