package com.intepy.bancapp.servicies;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intepy.bancapp.entities.Prestamo;
import com.intepy.bancapp.repositories.PrestamoRepository;

@Service
public class PrestamoService {

    @Autowired
    private PrestamoRepository prestamoRepository;

    public List<Prestamo> listarPrestamos() {
        return prestamoRepository.findAll();
    }

    public Optional<Prestamo> obtenerPrestamoPorId(Long id) {
        return prestamoRepository.findById(id);
    }

    public Prestamo guardarPrestamo(Prestamo prestamo) {
        return prestamoRepository.save(prestamo);
    }

    public Prestamo actualizarPrestamo(Long id, Prestamo prestamoActualizado) {
        return prestamoRepository.findById(id)
                .map(prestamo -> {
                    prestamo.setMonto(prestamoActualizado.getMonto());
                    prestamo.setEstado(prestamoActualizado.getEstado());
                    return prestamoRepository.save(prestamo);
                })
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));
    }

    public void eliminarPrestamo(Long id) {
        prestamoRepository.deleteById(id);
    }
}
