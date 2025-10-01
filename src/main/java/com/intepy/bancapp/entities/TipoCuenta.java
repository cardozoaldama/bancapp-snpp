package com.intepy.bancapp.entities;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.intepy.bancapp.entities.enums.TipoCuentaBasica;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class TipoCuenta {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private TipoCuentaBasica nombre;

    @Getter
    @OneToMany(mappedBy = "tipoCuenta", cascade = CascadeType.ALL)
    private List<Cuenta> cuentas;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public TipoCuenta(TipoCuentaBasica nombre) {
        this.nombre = nombre;
    }

    public void addCuenta(Cuenta cuenta) {
        cuentas.add(cuenta);
    }

    public void removeCuenta(Cuenta cuenta) {
        cuentas.remove(cuenta);
    }

}
