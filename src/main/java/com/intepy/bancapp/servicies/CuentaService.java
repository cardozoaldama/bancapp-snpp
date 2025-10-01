package com.intepy.bancapp.servicies;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intepy.bancapp.entities.Cuenta;
import com.intepy.bancapp.entities.Usuario;
import com.intepy.bancapp.exceptions.EntityNotFoundException;
import com.intepy.bancapp.repositories.CuentaRepository;
import com.intepy.bancapp.repositories.UsuarioRepository;

@Service
public class CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Cuenta> listarCuentas() {
        return cuentaRepository.findAll();
    }

    public Optional<Cuenta> obtenerCuentaPorId(Long id) {
        return cuentaRepository.findById(id);
    }

    public Cuenta guardarCuenta(Cuenta cuenta) {
        // CORRECCIÓN: Si la cuenta tiene un usuario, buscar el usuario completo
        if (cuenta.getUsuario() != null && cuenta.getUsuario().getId() != null) {
            Long usuarioId = cuenta.getUsuario().getId();
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Usuario no encontrado con id: " + usuarioId));
            cuenta.setUsuario(usuario);
        }

        return cuentaRepository.save(cuenta);
    }

    public Cuenta actualizarCuenta(Long id, Cuenta cuentaActualizada) {
        return cuentaRepository.findById(id)
                .map(cuenta -> {
                    cuenta.setNumero(cuentaActualizada.getNumero());
                    cuenta.setSaldo(cuentaActualizada.getSaldo());
                    if (cuentaActualizada.getTipoCuenta() != null) {
                        cuenta.setTipoCuenta(cuentaActualizada.getTipoCuenta());
                    }

                    // CORRECCIÓN: Si se actualiza el usuario, buscar el usuario completo
                    if (cuentaActualizada.getUsuario() != null &&
                            cuentaActualizada.getUsuario().getId() != null) {
                        Long usuarioId = cuentaActualizada.getUsuario().getId();
                        Usuario usuario = usuarioRepository.findById(usuarioId)
                                .orElseThrow(() -> new EntityNotFoundException(
                                        "Usuario no encontrado con id: " + usuarioId));
                        cuenta.setUsuario(usuario);
                    }

                    return cuentaRepository.save(cuenta);
                })
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada con id: " + id));
    }

    public void eliminarCuenta(Long id) {
        cuentaRepository.deleteById(id);
    }
}