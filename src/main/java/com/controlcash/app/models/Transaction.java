package com.controlcash.app.models;

import com.controlcash.app.models.enums.TransactionType;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
public class Transaction implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String name;
    private String description;
    @Column(nullable = false)
    private Date createdDate;
    @Column(nullable = false, columnDefinition = "DOUBLE CHECK (value < 0)")
    private Double value;
    @Column(nullable = false, columnDefinition = "INT CHECK (amountRepeat <= 0)")
    private Integer amountRepeat;
    @Column(nullable = false)
    private Integer transactionType;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;

    public Transaction() {
    }

    public Transaction(UUID id, String name, String description, Date createdDate, Double value, Integer amountRepeat, TransactionType transactionType) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdDate = createdDate;
        this.value = value;
        this.amountRepeat = amountRepeat;
        this.transactionType = transactionType.getCode();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Integer getAmountRepeat() {
        return amountRepeat;
    }

    public void setAmountRepeat(Integer amountRepeat) {
        this.amountRepeat = amountRepeat;
    }

    public TransactionType getTransactionType() {
        return TransactionType.valueOf(this.transactionType);
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType.getCode();
    }

    public User getUser() {
        return user;
    }
}
