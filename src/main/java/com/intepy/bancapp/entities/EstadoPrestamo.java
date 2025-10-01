package com.intepy.bancapp.entities;

import java.util.List;

import com.intepy.bancapp.entities.enums.EstadoPrestamoDescripcion;

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
public class EstadoPrestamo {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private EstadoPrestamoDescripcion descripcion;

    @Getter
    @OneToMany(mappedBy = "estado")
    private List<Prestamo> prestamos;

    public EstadoPrestamo(EstadoPrestamoDescripcion descripcion) {
        this.descripcion = descripcion;
    }

    public void addPrestamo(Prestamo prestamo) {
        prestamos.add(prestamo);
    }

    public void removePrestamo(Prestamo prestamo) {
        prestamos.remove(prestamo);
    }

}
