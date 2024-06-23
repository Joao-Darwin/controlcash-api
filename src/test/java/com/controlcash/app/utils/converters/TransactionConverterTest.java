package com.controlcash.app.utils.converters;

import com.controlcash.app.dtos.request.TransactionCreateRequestDTO;
import com.controlcash.app.models.Category;
import com.controlcash.app.models.Transaction;
import com.controlcash.app.models.enums.TransactionType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TransactionConverterTest {

    @Test
    public void testConverterTransactionCreateResponseDTOToTransaction_ShouldReturnATransaction() {
        String expectedName = "Month Salary";
        String expectedDescription = "my month salary from job";
        double expectedValue = 1600.00;
        int expectedAmountRepeat = 1;
        TransactionType expectedTransactionType = TransactionType.ENTRANCE;
        TransactionCreateRequestDTO transactionCreateRequestDTO = new TransactionCreateRequestDTO(expectedName, expectedDescription, expectedValue, expectedAmountRepeat, expectedTransactionType, List.of(new Category()));

        Transaction transaction = TransactionConverter.convertTransactionCreateRequestDTOToTransaction(transactionCreateRequestDTO);

        Assertions.assertEquals(expectedName, transaction.getName());
        Assertions.assertEquals(expectedDescription, transaction.getDescription());
        Assertions.assertEquals(expectedValue, transaction.getValue());
        Assertions.assertEquals(expectedAmountRepeat, transaction.getAmountRepeat());
        Assertions.assertEquals(expectedTransactionType, transaction.getTransactionType());
    }
}
