package com.intepy.bancapp.entities;

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
public class PagoServicio {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private Double monto;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "cuenta_id")
    private Cuenta cuenta;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "servicio_id")
    private Servicio servicio;

    public PagoServicio(Double monto, Cuenta cuenta, Servicio servicio) {
        this.monto = monto;
        this.cuenta = cuenta;
        this.servicio = servicio;
    }

}
