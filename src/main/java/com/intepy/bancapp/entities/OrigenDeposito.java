package com.intepy.bancapp.entities;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public OrigenDeposito(String descripcion) {
        this.descripcion = descripcion;
    }

    public void addDeposito(Deposito deposito) {
        depositos.add(deposito);
    }

    public void removeDeposito(Deposito deposito) {
        depositos.remove(deposito);
    }
}
