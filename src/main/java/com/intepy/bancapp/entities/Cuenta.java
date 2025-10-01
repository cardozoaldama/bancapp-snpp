package com.intepy.bancapp.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.intepy.bancapp.entities.enums.TipoCuentaBasica;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    private Double saldo = 0.0;

    @Getter
    @Setter
    @JsonBackReference("usuario-cuentas")
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private TipoCuentaBasica tipoCuenta;

    @Getter
    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL)
    private List<Deposito> depositos = new ArrayList<>();

    @Getter
    @OneToMany(mappedBy = "cuentaOrigen", cascade = CascadeType.ALL)
    private List<Transferencia> transferenciasOrigen = new ArrayList<>();

    @Getter
    @OneToMany(mappedBy = "cuentaDestino", cascade = CascadeType.ALL)
    private List<Transferencia> transferenciasDestino = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "cuenta")
    private List<PagoServicio> pagos = new ArrayList<>();

    public Cuenta(String numeroCuenta, Double saldo, Usuario usuario, TipoCuentaBasica tipoCuenta) {
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

    public void addPago(PagoServicio pagoServicio) {
        pagos.add(pagoServicio);
    }

    public void removePago(PagoServicio pagoServicio) {
        pagos.remove(pagoServicio);
    }
}
