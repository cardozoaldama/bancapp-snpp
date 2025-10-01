package com.intepy.bancapp.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class Usuario {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @NotBlank(message = "El nombre es requerido")
    private String nombre;

    @Getter
    @Setter
    @Email(message = "Email debe ser válido")
    @NotBlank(message = "El email es requerido")
    private String email;

    @Getter
    @JsonManagedReference("usuario-cuentas")
    @OneToMany(mappedBy = "usuario")
    private List<Cuenta> cuentas = new ArrayList<>();

    @Getter
    @JsonManagedReference("usuario-prestamos")
    @OneToMany(mappedBy = "usuario")
    private List<Prestamo> prestamos = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Usuario(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }

    public void addCuenta(Cuenta cuenta) {
        cuentas.add(cuenta);
    }

    public void removeCuenta(Cuenta cuenta) {
        cuentas.remove(cuenta);
    }

    public void addPrestamo(Prestamo prestamo) {
        prestamos.add(prestamo);
    }

    public void removePrestamo(Prestamo prestamo) {
        prestamos.remove(prestamo);
    }
}
