package com.intepy.bancapp.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.intepy.bancapp.entities.enums.AccountType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Account {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @NotBlank(message = "Account number is required")
    private String number;

    @Getter
    @Setter
    @NotNull(message = "Balance is required")
    @Min(value = 0, message = "Balance cannot be negative")
    private Double balance = 0.0;

    @Getter
    @Setter
    @JsonBackReference("user-accounts")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Getter
    @JsonManagedReference("account-deposits")
    @OneToMany(mappedBy = "account")
    private List<Deposit> deposits = new ArrayList<>();

    @Getter
    @OneToMany(mappedBy = "sourceAccount")
    private List<Transfer> outgoingTransfers = new ArrayList<>();

    @Getter
    @OneToMany(mappedBy = "destinationAccount")
    private List<Transfer> incomingTransfers = new ArrayList<>();

    @Getter
    @JsonManagedReference("account-payments")
    @OneToMany(mappedBy = "account")
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

    public Account(String number, Double balance, User user, AccountType accountType) {
        this.number = number;
        this.balance = balance;
        this.user = user;
        this.accountType = accountType;
    }

    public void addDeposit(Deposit deposit) {
        deposits.add(deposit);
    }

    public void removeDeposit(Deposit deposit) {
        deposits.remove(deposit);
    }

    public void addOutgoingTransfer(Transfer transfer) {
        outgoingTransfers.add(transfer);
    }

    public void removeOutgoingTransfer(Transfer transfer) {
        outgoingTransfers.remove(transfer);
    }

    public void addIncomingTransfer(Transfer transfer) {
        incomingTransfers.add(transfer);
    }

    public void removeIncomingTransfer(Transfer transfer) {
        incomingTransfers.remove(transfer);
    }

    public void addPayment(ServicePayment servicePayment) {
        payments.add(servicePayment);
    }

    public void removePayment(ServicePayment servicePayment) {
        payments.remove(servicePayment);
    }
}
