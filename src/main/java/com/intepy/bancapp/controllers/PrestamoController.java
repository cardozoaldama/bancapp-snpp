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

import com.intepy.bancapp.entities.Prestamo;
import com.intepy.bancapp.servicies.PrestamoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    @GetMapping
    public List<Prestamo> listarPrestamos() {
        return prestamoService.listarPrestamos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prestamo> obtenerPrestamo(@PathVariable Long id) {
        return prestamoService.obtenerPrestamoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Prestamo> crearPrestamo(@Valid @RequestBody Prestamo prestamo) {
        Prestamo prestamoCreado = prestamoService.guardarPrestamo(prestamo);
        return ResponseEntity
                .created(URI.create("/api/prestamos/" + prestamoCreado.getId()))
                .body(prestamoCreado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Prestamo> actualizarPrestamo(@PathVariable Long id, @Valid @RequestBody Prestamo prestamo) {
        Prestamo prestamoActualizado = prestamoService.actualizarPrestamo(id, prestamo);
        return ResponseEntity.ok(prestamoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPrestamo(@PathVariable Long id) {
        prestamoService.eliminarPrestamo(id);
        return ResponseEntity.noContent().build();
    }
}