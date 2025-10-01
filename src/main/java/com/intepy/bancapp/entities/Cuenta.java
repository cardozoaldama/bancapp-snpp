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

    @OneToMany(mappedBy = "cuenta")
    private List<PagoServicio> pagos = new ArrayList<>();

    public Cuenta(String numeroCuenta, Double saldo, Usuario usuario, TipoCuenta tipoCuenta) {
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldo;
        this.usuario = usuario;
        this.tipoCuenta = tipoCuenta;
    }

    public void addDeposito(Deposito deposito) {
        depositos.add(deposito);
    }

    public void removeDeposito(Deposito deposito) {
        depositos.remove(deposito);
    }

    public void addTransferenciasOrigen(Transferencia transferencia) {
        transferenciasOrigen.add(transferencia);
    }

    public void removeTransferenciasOrigen(Transferencia transferencia) {
        transferenciasOrigen.remove(transferencia);
    }

    public void addTransferenciasDestino(Transferencia transferencia) {
        transferenciasDestino.add(transferencia);
    }

    public void removeTransferenciasDestino(Transferencia transferencia) {
        transferenciasDestino.remove(transferencia);
    }
}
