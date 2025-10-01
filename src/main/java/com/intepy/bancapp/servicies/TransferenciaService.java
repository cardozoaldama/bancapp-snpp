package com.intepy.bancapp.servicies;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intepy.bancapp.entities.Cuenta;
import com.intepy.bancapp.entities.Transferencia;
import com.intepy.bancapp.exceptions.EntityNotFoundException;
import com.intepy.bancapp.exceptions.InsufficientBalanceException;
import com.intepy.bancapp.exceptions.InvalidTransferException;
import com.intepy.bancapp.repositories.CuentaRepository;
import com.intepy.bancapp.repositories.TransferenciaRepository;

@Service
public class TransferenciaService {

    @Autowired
    private TransferenciaRepository transferenciaRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    public List<Transferencia> listarTransferencias() {
        return transferenciaRepository.findAll();
    }

    public Optional<Transferencia> obtenerTransferenciaPorId(Long id) {
        return transferenciaRepository.findById(id);
    }

    @Transactional
    public Transferencia guardarTransferencia(Transferencia transferencia) {
        Cuenta cuentaOrigen = transferencia.getCuentaOrigen();
        Cuenta cuentaDestino = transferencia.getCuentaDestino();
        Double monto = transferencia.getMonto();

        // Validar que las cuentas existan
        if (cuentaOrigen == null || cuentaDestino == null) {
            throw new InvalidTransferException("Las cuentas de origen y destino son requeridas");
        }

        // Validar que la cuenta origen tenga saldo suficiente
        if (cuentaOrigen.getSaldo() < monto) {
            throw new InsufficientBalanceException(
                    "Saldo insuficiente en la cuenta origen. Saldo actual: " + cuentaOrigen.getSaldo());
        }

        // Validar que no sea la misma cuenta
        if (cuentaOrigen.getId().equals(cuentaDestino.getId())) {
            throw new InvalidTransferException("La cuenta origen y destino no pueden ser la misma");
        }

        // Realizar la transferencia
        cuentaOrigen.setSaldo(cuentaOrigen.getSaldo() - monto);
        cuentaDestino.setSaldo(cuentaDestino.getSaldo() + monto);

        // Guardar las cuentas actualizadas
        cuentaRepository.save(cuentaOrigen);
        cuentaRepository.save(cuentaDestino);

        // Guardar la transferencia
        return transferenciaRepository.save(transferencia);
    }

    @Transactional
    public Transferencia actualizarTransferencia(Long id, Transferencia transferenciaActualizada) {
        return transferenciaRepository.findById(id)
                .map(transferencia -> {
                    // Revertir la transferencia anterior
                    Cuenta cuentaOrigenAnterior = transferencia.getCuentaOrigen();
                    Cuenta cuentaDestinoAnterior = transferencia.getCuentaDestino();
                    Double montoAnterior = transferencia.getMonto();

                    if (cuentaOrigenAnterior != null && cuentaDestinoAnterior != null) {
                        cuentaOrigenAnterior.setSaldo(cuentaOrigenAnterior.getSaldo() + montoAnterior);
                        cuentaDestinoAnterior.setSaldo(cuentaDestinoAnterior.getSaldo() - montoAnterior);
                        cuentaRepository.save(cuentaOrigenAnterior);
                        cuentaRepository.save(cuentaDestinoAnterior);
                    }

                    // Aplicar la nueva transferencia
                    Cuenta nuevaCuentaOrigen = transferenciaActualizada.getCuentaOrigen();
                    Cuenta nuevaCuentaDestino = transferenciaActualizada.getCuentaDestino();
                    Double nuevoMonto = transferenciaActualizada.getMonto();

                    // Validar saldo suficiente
                    if (nuevaCuentaOrigen.getSaldo() < nuevoMonto) {
                        throw new InsufficientBalanceException("Saldo insuficiente en la cuenta origen");
                    }

                    nuevaCuentaOrigen.setSaldo(nuevaCuentaOrigen.getSaldo() - nuevoMonto);
                    nuevaCuentaDestino.setSaldo(nuevaCuentaDestino.getSaldo() + nuevoMonto);

                    cuentaRepository.save(nuevaCuentaOrigen);
                    cuentaRepository.save(nuevaCuentaDestino);

                    // Actualizar la transferencia
                    transferencia.setMonto(nuevoMonto);
                    transferencia.setCuentaOrigen(nuevaCuentaOrigen);
                    transferencia.setCuentaDestino(nuevaCuentaDestino);

                    return transferenciaRepository.save(transferencia);
                })
                .orElseThrow(() -> new EntityNotFoundException("Transferencia no encontrada con id: " + id));
    }

    @Transactional
    public void eliminarTransferencia(Long id) {
        Optional<Transferencia> transferenciaOpt = transferenciaRepository.findById(id);
        if (transferenciaOpt.isPresent()) {
            Transferencia transferencia = transferenciaOpt.get();

            // Revertir la transferencia
            Cuenta cuentaOrigen = transferencia.getCuentaOrigen();
            Cuenta cuentaDestino = transferencia.getCuentaDestino();
            Double monto = transferencia.getMonto();

            if (cuentaOrigen != null && cuentaDestino != null) {
                cuentaOrigen.setSaldo(cuentaOrigen.getSaldo() + monto);
                cuentaDestino.setSaldo(cuentaDestino.getSaldo() - monto);

                cuentaRepository.save(cuentaOrigen);
                cuentaRepository.save(cuentaDestino);
            }
        }

        transferenciaRepository.deleteById(id);
    }
}