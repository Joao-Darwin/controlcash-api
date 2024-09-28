package com.controlcash.app.models;

import com.controlcash.app.models.enums.TransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "transactions")
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
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate createdDate;
    @Column(name = "transaction_value", nullable = false, columnDefinition = "FLOAT CHECK (transaction_value > 0)")
    private Double value;
    @Column(nullable = false, columnDefinition = "INT CHECK (amount_repeat >= 0)")
    private Integer amountRepeat;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "transaction_category", joinColumns = {@JoinColumn(name = "transaction_id")}, inverseJoinColumns = {@JoinColumn(name = "category_id")})
    private List<Category> categories;

    public Transaction() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
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
        return this.transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
