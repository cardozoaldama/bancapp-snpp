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

import com.intepy.bancapp.entities.Service;
import com.intepy.bancapp.services.BillService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    @Autowired
    private BillService billService;

    @GetMapping
    public List<Service> listBills() {
        return billService.listServices();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Service> getBill(@PathVariable Long id) {
        return billService.getServiceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Service> createBill(@Valid @RequestBody Service service) {
        Service createdBill = billService.saveService(service);
        return ResponseEntity
                .created(URI.create("/api/bills/" + createdBill.getId()))
                .body(createdBill);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Service> updateBill(@PathVariable Long id, @Valid @RequestBody Service service) {
        Service updatedBill = billService.updateService(id, service);
        return ResponseEntity.ok(updatedBill);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable Long id) {
        billService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}
