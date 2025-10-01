package com.intepy.bancapp.servicies;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intepy.bancapp.entities.Cuenta;
import com.intepy.bancapp.entities.PagoServicio;
import com.intepy.bancapp.exceptions.EntityNotFoundException;
import com.intepy.bancapp.exceptions.InsufficientBalanceException;
import com.intepy.bancapp.exceptions.ValidationException;
import com.intepy.bancapp.repositories.CuentaRepository;
import com.intepy.bancapp.repositories.PagoServicioRepository;

@Service
public class PagoServicioService {

    @Autowired
    private PagoServicioRepository pagoServicioRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    public List<PagoServicio> listarPagos() {
        return pagoServicioRepository.findAll();
    }

    public Optional<PagoServicio> obtenerPagoPorId(Long id) {
        return pagoServicioRepository.findById(id);
    }

    @Transactional
    public PagoServicio guardarPago(PagoServicio pago) {
        Cuenta cuenta = pago.getCuenta();
        Double monto = pago.getMonto();

        // Validar que la cuenta exista
        if (cuenta == null) {
            throw new ValidationException("La cuenta es requerida para realizar el pago");
        }

        // Validar que el servicio exista
        if (pago.getServicio() == null) {
            throw new ValidationException("El servicio es requerido para realizar el pago");
        }

        // Validar que la cuenta tenga saldo suficiente
        if (cuenta.getSaldo() < monto) {
            throw new InsufficientBalanceException(
                    "Saldo insuficiente para realizar el pago del servicio. Saldo actual: " + cuenta.getSaldo());
        }

        // Descontar el monto de la cuenta
        cuenta.setSaldo(cuenta.getSaldo() - monto);
        cuentaRepository.save(cuenta);

        // Guardar el pago
        return pagoServicioRepository.save(pago);
    }

    @Transactional
    public PagoServicio actualizarPago(Long id, PagoServicio pagoActualizado) {
        return pagoServicioRepository.findById(id)
                .map(pago -> {
                    // Revertir el pago anterior
                    Cuenta cuentaAnterior = pago.getCuenta();
                    Double montoAnterior = pago.getMonto();

                    if (cuentaAnterior != null) {
                        cuentaAnterior.setSaldo(cuentaAnterior.getSaldo() + montoAnterior);
                        cuentaRepository.save(cuentaAnterior);
                    }

                    // Aplicar el nuevo pago
                    Cuenta nuevaCuenta = pagoActualizado.getCuenta();
                    Double nuevoMonto = pagoActualizado.getMonto();

                    // Validar saldo suficiente
                    if (nuevaCuenta.getSaldo() < nuevoMonto) {
                        throw new InsufficientBalanceException("Saldo insuficiente para el nuevo monto del pago");
                    }

                    nuevaCuenta.setSaldo(nuevaCuenta.getSaldo() - nuevoMonto);
                    cuentaRepository.save(nuevaCuenta);

                    // Actualizar el pago
                    pago.setMonto(nuevoMonto);
                    pago.setCuenta(nuevaCuenta);
                    pago.setServicio(pagoActualizado.getServicio());

                    return pagoServicioRepository.save(pago);
                })
                .orElseThrow(() -> new EntityNotFoundException("Pago no encontrado con id: " + id));
    }

    @Transactional
    public void eliminarPago(Long id) {
        Optional<PagoServicio> pagoOpt = pagoServicioRepository.findById(id);
        if (pagoOpt.isPresent()) {
            PagoServicio pago = pagoOpt.get();

            // Revertir el pago - devolver el dinero a la cuenta
            Cuenta cuenta = pago.getCuenta();
            if (cuenta != null) {
                cuenta.setSaldo(cuenta.getSaldo() + pago.getMonto());
                cuentaRepository.save(cuenta);
            }
        }

        pagoServicioRepository.deleteById(id);
    }
}