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

import com.intepy.bancapp.dto.TransferRequestDTO;
import com.intepy.bancapp.entities.Transfer;
import com.intepy.bancapp.services.TransferService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    @Autowired
    private TransferService transferService;

    @GetMapping
    public List<Transfer> listTransfers() {
        return transferService.listTransfers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transfer> getTransfer(@PathVariable Long id) {
        return transferService.getTransferById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Transfer> createTransfer(@Valid @RequestBody Transfer transfer) {
        Transfer createdTransfer = transferService.saveTransfer(transfer);
        return ResponseEntity
                .created(URI.create("/api/transfers/" + createdTransfer.getId()))
                .body(createdTransfer);
    }

    @PostMapping("/simple")
    public ResponseEntity<Transfer> createTransferFromDTO(@Valid @RequestBody TransferRequestDTO dto) {
        Transfer createdTransfer = transferService.createTransferFromDTO(dto);
        return ResponseEntity
                .created(URI.create("/api/transfers/" + createdTransfer.getId()))
                .body(createdTransfer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transfer> updateTransfer(@PathVariable Long id,
            @Valid @RequestBody Transfer transfer) {
        Transfer updatedTransfer = transferService.updateTransfer(id, transfer);
        return ResponseEntity.ok(updatedTransfer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransfer(@PathVariable Long id) {
        transferService.deleteTransfer(id);
        return ResponseEntity.noContent().build();
    }
}
