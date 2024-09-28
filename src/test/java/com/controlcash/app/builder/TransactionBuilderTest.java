package com.controlcash.app.builder;

import com.controlcash.app.models.Category;
import com.controlcash.app.models.Transaction;
import com.controlcash.app.models.User;
import com.controlcash.app.models.enums.TransactionType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TransactionBuilderTest {

    @Test
    void testTransactionBuilder_GivenAllParams_ShouldReturnATransaction() {
        TransactionType expectedTransactionType = TransactionType.PAYMENT;
        int expectedAmountRepeat = 3;
        String expectedDescription = "I get God of War in promotion on Steam";
        String expectedName = "God of War";
        Double expectedValue = 59.99;
        LocalDate expectedCreatedDate = LocalDate.now();
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

    @Test
    void testTransactionBuilder_PartialParams_ShouldReturnTransactionWithDefaultValues() {
        TransactionType expectedTransactionType = TransactionType.PAYMENT;
        String expectedName = "God of War";
        TransactionBuilder transactionBuilder = new TransactionBuilder(expectedTransactionType);

        Transaction transaction = transactionBuilder
                .addName(expectedName)
                .build();

        Assertions.assertNotNull(transaction);
        Assertions.assertEquals(expectedTransactionType, transaction.getTransactionType());
        Assertions.assertEquals(expectedName, transaction.getName());
        Assertions.assertNull(transaction.getDescription());
        Assertions.assertNull(transaction.getCreatedDate());
        Assertions.assertNull(transaction.getId());
        Assertions.assertNull(transaction.getValue());
        Assertions.assertNull(transaction.getUser());
        Assertions.assertTrue(transaction.getCategories().isEmpty());
    }

    @Test
    void testTransactionBuilder_ImmutableTransaction_ShouldThrowExceptionOnModification() {
        TransactionBuilder transactionBuilder = new TransactionBuilder(TransactionType.PAYMENT);
        Transaction transaction = transactionBuilder.build();

        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            transaction.getCategories().add(new Category());
        });
    }

}
