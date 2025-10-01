package com.intepy.bancapp.servicies;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intepy.bancapp.entities.Prestamo;
import com.intepy.bancapp.entities.Usuario;
import com.intepy.bancapp.exceptions.EntityNotFoundException;
import com.intepy.bancapp.repositories.PrestamoRepository;
import com.intepy.bancapp.repositories.UsuarioRepository;

@Service
public class PrestamoService {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Prestamo> listarPrestamos() {
        return prestamoRepository.findAll();
    }

    public Optional<Prestamo> obtenerPrestamoPorId(Long id) {
        return prestamoRepository.findById(id);
    }

    public Prestamo guardarPrestamo(Prestamo prestamo) {
        // CORRECCIÓN: Si el préstamo tiene un usuario, buscar el usuario completo
        if (prestamo.getUsuario() != null && prestamo.getUsuario().getId() != null) {
            Long usuarioId = prestamo.getUsuario().getId();
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Usuario no encontrado con id: " + usuarioId));
            prestamo.setUsuario(usuario);
        }

        return prestamoRepository.save(prestamo);
    }

    public Prestamo actualizarPrestamo(Long id, Prestamo prestamoActualizado) {
        return prestamoRepository.findById(id)
                .map(prestamo -> {
                    prestamo.setMonto(prestamoActualizado.getMonto());
                    prestamo.setEstado(prestamoActualizado.getEstado());

                    // CORRECCIÓN: Si se actualiza el usuario, buscar el usuario completo
                    if (prestamoActualizado.getUsuario() != null &&
                            prestamoActualizado.getUsuario().getId() != null) {
                        Long usuarioId = prestamoActualizado.getUsuario().getId();
                        Usuario usuario = usuarioRepository.findById(usuarioId)
                                .orElseThrow(() -> new EntityNotFoundException(
                                        "Usuario no encontrado con id: " + usuarioId));
                        prestamo.setUsuario(usuario);
                    }

                    return prestamoRepository.save(prestamo);
                })
                .orElseThrow(() -> new EntityNotFoundException("Préstamo no encontrado con id: " + id));
    }

    public void eliminarPrestamo(Long id) {
        prestamoRepository.deleteById(id);
    }
}