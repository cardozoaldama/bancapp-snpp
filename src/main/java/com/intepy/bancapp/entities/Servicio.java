package com.intepy.bancapp.entities;

import java.util.List;

import com.intepy.bancapp.entities.enums.TipoServicio;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Servicio {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private TipoServicio nombre;

    @Getter
    @OneToMany(mappedBy = "servicio")
    private List<PagoServicio> pagos;
}
