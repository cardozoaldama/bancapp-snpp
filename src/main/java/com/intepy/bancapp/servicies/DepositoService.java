package com.intepy.bancapp.servicies;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intepy.bancapp.entities.Cuenta;
import com.intepy.bancapp.entities.Deposito;
import com.intepy.bancapp.exceptions.EntityNotFoundException;
import com.intepy.bancapp.repositories.CuentaRepository;
import com.intepy.bancapp.repositories.DepositoRepository;

import jakarta.transaction.Transactional;

@Service
public class DepositoService {

    @Autowired
    private DepositoRepository depositoRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    public List<Deposito> listarDepositos() {
        return depositoRepository.findAll();
    }

    public Optional<Deposito> obtenerDepositoPorId(Long id) {
        return depositoRepository.findById(id);
    }

    @Transactional
    public Deposito guardarDeposito(Deposito deposito) {
        // CORRECCIÓN: Buscar la cuenta completa de la base de datos
        if (deposito.getCuenta() == null || deposito.getCuenta().getId() == null) {
            throw new EntityNotFoundException("La cuenta es requerida");
        }

        Long cuentaId = deposito.getCuenta().getId();
        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada con id: " + cuentaId));

        // Actualizar el saldo de la cuenta
        cuenta.setSaldo(cuenta.getSaldo() + deposito.getMonto());
        cuentaRepository.save(cuenta);

        // Asociar la cuenta completa al depósito
        deposito.setCuenta(cuenta);

        return depositoRepository.save(deposito);
    }

    @Transactional
    public Deposito actualizarDeposito(Long id, Deposito depositoActualizado) {
        return depositoRepository.findById(id)
                .map(deposito -> {
                    // Si el monto cambió, ajustar el saldo de la cuenta
                    if (!deposito.getMonto().equals(depositoActualizado.getMonto())) {
                        Cuenta cuenta = deposito.getCuenta();
                        if (cuenta != null) {
                            // Revertir el depósito anterior y aplicar el nuevo
                            double diferencia = depositoActualizado.getMonto() - deposito.getMonto();
                            cuenta.setSaldo(cuenta.getSaldo() + diferencia);
                            cuentaRepository.save(cuenta);
                        }
                    }

                    deposito.setMonto(depositoActualizado.getMonto());

                    // CORRECCIÓN: Si se cambia la cuenta, buscar la nueva cuenta completa
                    if (depositoActualizado.getCuenta() != null &&
                            depositoActualizado.getCuenta().getId() != null) {
                        Long nuevaCuentaId = depositoActualizado.getCuenta().getId();
                        Cuenta nuevaCuenta = cuentaRepository.findById(nuevaCuentaId)
                                .orElseThrow(() -> new EntityNotFoundException(
                                        "Cuenta no encontrada con id: " + nuevaCuentaId));
                        deposito.setCuenta(nuevaCuenta);
                    }

                    return depositoRepository.save(deposito);
                })
                .orElseThrow(() -> new EntityNotFoundException("Depósito no encontrado con id: " + id));
    }

    @Transactional
    public void eliminarDeposito(Long id) {
        Optional<Deposito> depositoOpt = depositoRepository.findById(id);
        if (depositoOpt.isPresent()) {
            Deposito deposito = depositoOpt.get();
            // Revertir el depósito del saldo de la cuenta
            Cuenta cuenta = deposito.getCuenta();
            if (cuenta != null) {
                cuenta.setSaldo(cuenta.getSaldo() - deposito.getMonto());
                cuentaRepository.save(cuenta);
            }
        }
        depositoRepository.deleteById(id);
    }
}