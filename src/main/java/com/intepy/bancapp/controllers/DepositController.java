package com.intepy.bancapp.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intepy.bancapp.entities.Deposit;
import com.intepy.bancapp.services.DepositService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/deposits")
public class DepositController {

    @Autowired
    private DepositService depositService;

    @GetMapping
    public List<Deposit> listDeposits() {
        return depositService.listDeposits();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Deposit> getDeposit(@PathVariable Long id) {
        return depositService.getDepositById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Deposit> createDeposit(@Valid @RequestBody Deposit deposit) {
        Deposit createdDeposit = depositService.saveDeposit(deposit);
        return ResponseEntity
                .created(URI.create("/api/deposits/" + createdDeposit.getId()))
                .body(createdDeposit);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Deposit> updateDeposit(@PathVariable Long id, @Valid @RequestBody Deposit deposit) {
        Deposit updatedDeposit = depositService.updateDeposit(id, deposit);
        return ResponseEntity.ok(updatedDeposit);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeposit(@PathVariable Long id) {
        depositService.deleteDeposit(id);
        return ResponseEntity.noContent().build();
    }
}
