package com.intepy.bancapp.servicies;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intepy.bancapp.entities.Cuenta;
import com.intepy.bancapp.repositories.CuentaRepository;

@Service
public class CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;

    public List<Cuenta> listarCuentas() {
        return cuentaRepository.findAll();
    }

    public Optional<Cuenta> obtenerCuentaPorId(Long id) {
        return cuentaRepository.findById(id);
    }

    public Cuenta guardarCuenta(Cuenta cuenta) {
        return cuentaRepository.save(cuenta);
    }

    public Cuenta actualizarCuenta(Long id, Cuenta cuentaActualizada) {
        return cuentaRepository.findById(id)
                .map(cuenta -> {
                    cuenta.setNumero(cuentaActualizada.getNumero());
                    cuenta.setSaldo(cuentaActualizada.getSaldo());
                    cuenta.setTipoCuenta(cuentaActualizada.getTipoCuenta());
                    return cuentaRepository.save(cuenta);
                })
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
    }

    public void eliminarCuenta(Long id) {
        cuentaRepository.deleteById(id);
    }
}
