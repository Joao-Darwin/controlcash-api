package com.controlcash.app.builder;

import com.controlcash.app.models.Category;
import com.controlcash.app.models.Transaction;
import com.controlcash.app.models.User;
import com.controlcash.app.models.enums.TransactionType;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TransactionBuilder {
    private final Transaction transaction;

    public TransactionBuilder(TransactionType transactionType) {
        transaction = new Transaction();
        transaction.setTransactionType(transactionType);
    }

    public TransactionBuilder addId(UUID id) {
        transaction.setId(id);
        return this;
    }

    public TransactionBuilder addName(String name) {
        transaction.setName(name);
        return this;
    }

    public TransactionBuilder addDescription(String description) {
        transaction.setDescription(description);
        return this;
    }

    public TransactionBuilder addCreatedDate(Date createdDate) {
        transaction.setCreatedDate(createdDate);
        return this;
    }

    public TransactionBuilder addValue(Double value) {
        transaction.setValue(value);
        return this;
    }

    public TransactionBuilder addAmountRepeat(Integer amountRepeat) {
        transaction.setAmountRepeat(amountRepeat);
        return this;
    }

    public TransactionBuilder addUser(User user) {
        transaction.setUser(user);
        return this;
    }

    public TransactionBuilder addCategories(List<Category> categories) {
        transaction.setCategories(categories);
        return this;
    }

    public Transaction build() {
        return transaction;
    }
}
