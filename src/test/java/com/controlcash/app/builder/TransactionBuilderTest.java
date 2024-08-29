package com.controlcash.app.builder;

import com.controlcash.app.models.Transaction;
import com.controlcash.app.models.User;
import com.controlcash.app.models.enums.TransactionType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TransactionBuilderTest {

    @Test
    void testTransactionBuilder_GivenAllParams_ShouldReturnATransaction() {
        TransactionType expectedTransactionType = TransactionType.PAYMENT;
        int expectedAmountRepeat = 3;
        String expectedDescription = "I get God of War in promotion on Steam";
        String expectedName = "God of War";
        Double expectedValue = 59.99;
        Date expectedCreatedDate = new Date();
        UUID expectedId = UUID.randomUUID();
        TransactionBuilder transactionBuilder = new TransactionBuilder(expectedTransactionType);

        Transaction transaction = transactionBuilder
                .addAmountRepeat(expectedAmountRepeat)
                .addDescription(expectedDescription)
                .addName(expectedName)
                .addValue(expectedValue)
                .addCreatedDate(expectedCreatedDate)
                .addId(expectedId)
                .addCategories(List.of())
                .addUser(new User())
                .build();

        Assertions.assertNotNull(transaction);
        Assertions.assertEquals(expectedTransactionType, transaction.getTransactionType());
        Assertions.assertEquals(expectedAmountRepeat, transaction.getAmountRepeat());
        Assertions.assertEquals(expectedDescription, transaction.getDescription());
        Assertions.assertEquals(expectedName, transaction.getName());
        Assertions.assertEquals(expectedValue, transaction.getValue());
        Assertions.assertEquals(expectedCreatedDate, transaction.getCreatedDate());
        Assertions.assertEquals(expectedId, transaction.getId());
    }
}
