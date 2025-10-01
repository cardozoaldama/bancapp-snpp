package com.intepy.bancapp.entities;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.intepy.bancapp.entities.enums.EstadoPrestamoDescripcion;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class Prestamo {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private Double monto;

    @Getter
    @Setter
    @JsonBackReference("usuario-prestamos")
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private EstadoPrestamoDescripcion estado;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Prestamo(Double monto, Usuario usuario, EstadoPrestamoDescripcion estado) {
        this.monto = monto;
        this.usuario = usuario;
        this.estado = estado;
    }

}
