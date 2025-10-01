package com.intepy.bancapp.entities;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class Transferencia {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @NotNull(message = "El monto es requerido")
    @Min(value = 1, message = "El monto debe ser mayor a 0")
    private Double monto;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "cuenta_origen_id")
    @NotNull(message = "La cuenta origen es requerida")
    private Cuenta cuentaOrigen;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "cuenta_destino_id")
    @NotNull(message = "La cuenta destino es requerida")
    private Cuenta cuentaDestino;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Transferencia(Double monto, Cuenta cuentaOrigen, Cuenta cuentaDestino) {
        this.monto = monto;
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
    }

}
