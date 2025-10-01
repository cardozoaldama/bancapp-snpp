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

import com.intepy.bancapp.entities.Deposito;
import com.intepy.bancapp.servicies.DepositoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/depositos")
public class DepositoController {

    @Autowired
    private DepositoService depositoService;

    @GetMapping
    public List<Deposito> listarDepositos() {
        return depositoService.listarDepositos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Deposito> obtenerDeposito(@PathVariable Long id) {
        return depositoService.obtenerDepositoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Deposito> crearDeposito(@Valid @RequestBody Deposito deposito) {
        Deposito depositoCreado = depositoService.guardarDeposito(deposito);
        return ResponseEntity
                .created(URI.create("/api/depositos/" + depositoCreado.getId()))
                .body(depositoCreado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Deposito> actualizarDeposito(@PathVariable Long id, @Valid @RequestBody Deposito deposito) {
        try {
            return ResponseEntity.ok(depositoService.actualizarDeposito(id, deposito));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDeposito(@PathVariable Long id) {
        depositoService.eliminarDeposito(id);
        return ResponseEntity.noContent().build();
    }
}