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

import com.intepy.bancapp.entities.ServicePayment;
import com.intepy.bancapp.services.ServicePaymentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payments")
public class ServicePaymentController {

    @Autowired
    private ServicePaymentService servicePaymentService;

    @GetMapping
    public List<ServicePayment> listPayments() {
        return servicePaymentService.listPayments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicePayment> getPayment(@PathVariable Long id) {
        return servicePaymentService.getPaymentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ServicePayment> createPayment(@Valid @RequestBody ServicePayment payment) {
        ServicePayment createdPayment = servicePaymentService.savePayment(payment);
        return ResponseEntity
                .created(URI.create("/api/payments/" + createdPayment.getId()))
                .body(createdPayment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicePayment> updatePayment(@PathVariable Long id, @Valid @RequestBody ServicePayment payment) {
        ServicePayment updatedPayment = servicePaymentService.updatePayment(id, payment);
        return ResponseEntity.ok(updatedPayment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        servicePaymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}
