package com.intepy.bancapp.entities;

import java.util.List;

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
}
