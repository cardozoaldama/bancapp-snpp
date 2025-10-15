package com.intepy.bancapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intepy.bancapp.entities.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
