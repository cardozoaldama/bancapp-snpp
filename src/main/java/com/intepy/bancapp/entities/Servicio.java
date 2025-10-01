package com.intepy.bancapp.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.intepy.bancapp.entities.enums.TipoServicio;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class Servicio {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @NotNull(message = "El tipo de servicio es requerido")
    private TipoServicio nombre;

    @Getter
    @OneToMany(mappedBy = "servicio")
    private List<PagoServicio> pagos = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Servicio(TipoServicio nombre) {
        this.nombre = nombre;
    }

    public void addPago(PagoServicio pago) {
        pagos.add(pago);
    }

    public void removePago(PagoServicio pago) {
        pagos.remove(pago);
    }
}
