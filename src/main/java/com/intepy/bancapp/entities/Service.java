package com.intepy.bancapp.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.intepy.bancapp.entities.enums.ServiceType;

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
public class Service {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Service type is required")
    private ServiceType name;

    @Getter
    @Setter
    private String description;

    @Getter
    @JsonManagedReference("service-payments")
    @OneToMany(mappedBy = "service")
    private List<ServicePayment> payments = new ArrayList<>();

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @Getter
    @Setter
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    @Getter
    @Setter
    private LocalDateTime updatedAt;

    public Service(ServiceType name) {
        this.name = name;
    }

    public Service(ServiceType name, String description) {
        this.name = name;
        this.description = description;
    }

    public void addPayment(ServicePayment payment) {
        payments.add(payment);
    }

    public void removePayment(ServicePayment payment) {
        payments.remove(payment);
    }
}
