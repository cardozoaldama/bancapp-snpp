package com.intepy.bancapp.entities;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
@EntityListeners(AuditingEntityListener.class)
public class Deposito {

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
    @JsonBackReference("cuenta-depositos")
    @ManyToOne
    @JoinColumn(name = "cuenta_id")
    @NotNull(message = "La cuenta es requerida")
    private Cuenta cuenta;

    @Getter
    @Setter
    private String descripcionOrigen;

    @CreatedDate
    @Column(nullable = false, updatable = false) // No puede ser null, no se puede actualizar
    @Getter
    @Setter
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false) // No puede ser null, pero sí se actualiza
    @Getter
    @Setter
    private LocalDateTime updatedAt;

    public Deposito(Double monto, Cuenta cuenta, String descripcionOrigen) {
        this.monto = monto;
        this.cuenta = cuenta;
        this.descripcionOrigen = descripcionOrigen;
    }

}
