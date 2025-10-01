package com.intepy.bancapp.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.intepy.bancapp.entities.enums.TipoServicio;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
@EntityListeners(AuditingEntityListener.class)
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
    @Column(nullable = false, updatable = false) // No puede ser null, no se puede actualizar
    @Getter
    @Setter
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false) // No puede ser null, pero sí se actualiza
    @Getter
    @Setter
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
