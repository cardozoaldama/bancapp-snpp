package com.intepy.bancapp.entities;

import java.util.List;

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
public class OrigenDeposito {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private String descripcion;

    @Getter
    @OneToMany(mappedBy = "origen")
    private List<Deposito> depositos;

    public OrigenDeposito(String descripcion) {
        this.descripcion = descripcion;
    }

    void addDeposito(Deposito deposito) {
        depositos.add(deposito);
    }

    void removeDeposito(Deposito deposito) {
        depositos.remove(deposito);
    }
}
