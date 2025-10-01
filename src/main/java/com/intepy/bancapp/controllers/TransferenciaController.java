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

import com.intepy.bancapp.entities.Transferencia;
import com.intepy.bancapp.servicies.TransferenciaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transferencias")
public class TransferenciaController {

    @Autowired
    private TransferenciaService transferenciaService;

    @GetMapping
    public List<Transferencia> listarTransferencias() {
        return transferenciaService.listarTransferencias();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transferencia> obtenerTransferencia(@PathVariable Long id) {
        return transferenciaService.obtenerTransferenciaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Transferencia> crearTransferencia(@Valid @RequestBody Transferencia transferencia) {
        Transferencia transferenciaCreada = transferenciaService.guardarTransferencia(transferencia);
        return ResponseEntity
                .created(URI.create("/api/transferencias/" + transferenciaCreada.getId()))
                .body(transferenciaCreada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transferencia> actualizarTransferencia(@PathVariable Long id,
            @Valid @RequestBody Transferencia transferencia) {
        Transferencia transferenciaActualizada = transferenciaService.actualizarTransferencia(id, transferencia);
        return ResponseEntity.ok(transferenciaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTransferencia(@PathVariable Long id) {
        transferenciaService.eliminarTransferencia(id);
        return ResponseEntity.noContent().build();
    }
}
