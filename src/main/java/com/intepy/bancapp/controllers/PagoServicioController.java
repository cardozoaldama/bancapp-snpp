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

import com.intepy.bancapp.entities.PagoServicio;
import com.intepy.bancapp.servicies.PagoServicioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pagos")
public class PagoServicioController {

    @Autowired
    private PagoServicioService pagoServicioService;

    @GetMapping
    public List<PagoServicio> listarPagos() {
        return pagoServicioService.listarPagos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoServicio> obtenerPago(@PathVariable Long id) {
        return pagoServicioService.obtenerPagoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PagoServicio> crearPago(@Valid @RequestBody PagoServicio pago) {
        PagoServicio pagoCreado = pagoServicioService.guardarPago(pago);
        return ResponseEntity
                .created(URI.create("/api/pagos/" + pagoCreado.getId()))
                .body(pagoCreado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagoServicio> actualizarPago(@PathVariable Long id, @Valid @RequestBody PagoServicio pago) {
        PagoServicio pagoActualizado = pagoServicioService.actualizarPago(id, pago);
        return ResponseEntity.ok(pagoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(@PathVariable Long id) {
        pagoServicioService.eliminarPago(id);
        return ResponseEntity.noContent().build();
    }
}