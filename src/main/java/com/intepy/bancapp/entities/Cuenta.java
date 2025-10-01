package com.intepy.bancapp.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class Cuenta {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private String numeroCuenta;

    @Getter
    @Setter
    private Double saldo;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "tipo_cuenta_id")
    private TipoCuenta tipoCuenta;

    @Getter
    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL)
    private List<Deposito> depositos = new ArrayList<>();

    @Getter
    @OneToMany(mappedBy = "cuentaOrigen", cascade = CascadeType.ALL)
    private List<Transferencia> transferenciasOrigen = new ArrayList<>();

    @Getter
    @OneToMany(mappedBy = "cuentaDestino", cascade = CascadeType.ALL)
    private List<Transferencia> transferenciasDestino = new ArrayList<>();
}
