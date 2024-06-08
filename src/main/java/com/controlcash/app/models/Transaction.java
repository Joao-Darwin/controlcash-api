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
    private Date createdDate;
    @Column(nullable = false, columnDefinition = "DOUBLE CHECK (value < 0)")
    private Double value;
    @Column(nullable = false, columnDefinition = "INT CHECK (amountRepeat <= 0)")
    private Integer amountRepeat;
    private Integer transactionType;

    public Transaction() {
    }

    public Transaction(UUID id, Date createdDate, Double value, Integer amountRepeat, TransactionType transactionType) {
        this.id = id;
        this.createdDate = createdDate;
        this.value = value;
        this.amountRepeat = amountRepeat;
        this.transactionType = transactionType.getCode();
    }

    public UUID getId() {
        return id;
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
}
