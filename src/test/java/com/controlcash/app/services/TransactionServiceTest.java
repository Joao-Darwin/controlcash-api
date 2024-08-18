package com.controlcash.app.services;

import com.controlcash.app.builder.TransactionBuilder;
import com.controlcash.app.dtos.transaction.request.TransactionCreateRequestDTO;
import com.controlcash.app.dtos.transaction.response.TransactionCreateResponseDTO;
import com.controlcash.app.models.Transaction;
import com.controlcash.app.models.User;
import com.controlcash.app.models.enums.TransactionType;
import com.controlcash.app.repositories.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        transaction = new TransactionBuilder(TransactionType.PAYMENT)
                .addId(UUID.randomUUID())
                .addName("Credit Card")
                .addDescription("Credit card from NullBank")
                .addValue(245.39)
                .addAmountRepeat(0)
                .addCreatedDate(new Date())
                .build();
    }

    @Test
    void testCreate_GivenATransactionCreateRequestDTO_ShouldReturnATransactionCreateResponseDTO() {
        TransactionCreateRequestDTO transactionCreateRequestDTO = new TransactionCreateRequestDTO(
                "Credit Card",
                "Credit card from NullBank",
                245.39,
                0,
                TransactionType.PAYMENT,
                new User(),
                List.of());
        Mockito.when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(transaction);

        TransactionCreateResponseDTO transactionCreateResponseDTO = transactionService.create(transactionCreateRequestDTO);

        Assertions.assertNotNull(transactionCreateResponseDTO);
        Assertions.assertNotNull(transactionCreateResponseDTO.id());
        Assertions.assertNotNull(transactionCreateResponseDTO.createdDate());
        Assertions.assertEquals(transactionCreateResponseDTO.name(), transactionCreateRequestDTO.name());
        Assertions.assertEquals(transactionCreateResponseDTO.description(), transactionCreateRequestDTO.description());
    }
}
