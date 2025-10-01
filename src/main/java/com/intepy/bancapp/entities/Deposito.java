package com.intepy.bancapp.entities;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
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
public class Deposito {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private Double monto;

    @Getter
    @Setter
    @JsonBackReference("cuenta-depositos")
    @ManyToOne
    @JoinColumn(name = "cuenta_id")
    private Cuenta cuenta;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "origen_id")
    private OrigenDeposito origen;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Deposito(Double monto, Cuenta cuenta, OrigenDeposito origen) {
        this.monto = monto;
        this.cuenta = cuenta;
        this.origen = origen;
    }

}
