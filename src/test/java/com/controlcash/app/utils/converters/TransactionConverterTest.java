package com.controlcash.app.utils.converters;

import com.controlcash.app.dtos.request.TransactionCreateRequestDTO;
import com.controlcash.app.dtos.response.TransactionCreateResponseDTO;
import com.controlcash.app.models.Category;
import com.controlcash.app.models.Transaction;
import com.controlcash.app.models.enums.TransactionType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.UUID;

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

    @Test
    public void testConverterTransactionToTransactionCreateResponseDTO_ShouldReturnATransactionCreateResponseDTO() {
        UUID expectedId = UUID.randomUUID();
        String expectedName = "Month Salary";
        String expectedDescription = "my month salary from job";
        Date expectedCreatedDate = new Date();
        double expectedValue = 1600.00;
        int expectedAmountRepeat = 1;
        TransactionType expectedTransactionType = TransactionType.ENTRANCE;
        Transaction transaction = new Transaction(expectedId, expectedName, expectedDescription, expectedCreatedDate, expectedValue, expectedAmountRepeat, expectedTransactionType);

        TransactionCreateResponseDTO transactionCreateResponseDTO = TransactionConverter.convertTransactionToTransactionCreateResponseDTO(transaction);

        Assertions.assertEquals(expectedId, transactionCreateResponseDTO.id());
        Assertions.assertEquals(expectedName, transactionCreateResponseDTO.name());
        Assertions.assertEquals(expectedDescription, transactionCreateResponseDTO.description());
        Assertions.assertEquals(expectedCreatedDate, transactionCreateResponseDTO.createdDate());
    }
}