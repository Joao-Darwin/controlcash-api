package com.controlcash.app.services;

import com.controlcash.app.builder.TransactionBuilder;
import com.controlcash.app.dtos.transaction.request.TransactionCreateRequestDTO;
import com.controlcash.app.dtos.transaction.response.TransactionCompleteResponseDTO;
import com.controlcash.app.dtos.transaction.response.TransactionCreateResponseDTO;
import com.controlcash.app.exceptions.TransactionNotFoundException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private UUID id;
    private Transaction transaction;
    private String expectedTransactionNotFoundExceptionMessage;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        expectedTransactionNotFoundExceptionMessage = "Transaction not found. Id used: " + id;
        transaction = new TransactionBuilder(TransactionType.PAYMENT)
                .addId(id)
                .addName("Credit Card")
                .addDescription("Credit card from NullBank")
                .addValue(245.39)
                .addAmountRepeat(0)
                .addCreatedDate(LocalDate.now())
                .addCategories(List.of())
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

    @Test
    void testFindAll_GivenAPageable_ShouldReturnAPageWithTransactionCreateResponseDTO() {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "name");
        Transaction transaction2 = new TransactionBuilder(TransactionType.ENTRANCE)
                .addId(UUID.randomUUID())
                .addName("Salary")
                .addDescription("My salary from job")
                .addValue(1459.99)
                .addAmountRepeat(0)
                .addCreatedDate(LocalDate.now())
                .build();
        Page<Transaction> transactionPage = new PageImpl<>(List.of(transaction, transaction2));
        Mockito.when(transactionRepository.findAll(Mockito.any(Pageable.class))).thenReturn(transactionPage);

        Page<TransactionCreateResponseDTO> actualTransactionPage = transactionService.findAll(pageable);

        Assertions.assertNotNull(actualTransactionPage);
        Assertions.assertEquals(2, actualTransactionPage.getSize());
    }

    @Test
    void testFindById_GivenAnExistingId_ShouldReturnATransactionCompleteResponseDTO() {
        Mockito.when(transactionRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(transaction));

        TransactionCompleteResponseDTO actualTransactionCompleteResponseDTO = Assertions.assertDoesNotThrow(() -> transactionService.findById(UUID.randomUUID()));

        Assertions.assertNotNull(actualTransactionCompleteResponseDTO);
        Assertions.assertEquals(actualTransactionCompleteResponseDTO.id(), transaction.getId());
        Assertions.assertEquals(actualTransactionCompleteResponseDTO.name(), transaction.getName());
        Assertions.assertEquals(actualTransactionCompleteResponseDTO.description(), transaction.getDescription());
        Assertions.assertEquals(actualTransactionCompleteResponseDTO.createdDate(), transaction.getCreatedDate());
        Assertions.assertEquals(actualTransactionCompleteResponseDTO.value(), transaction.getValue());
        Assertions.assertEquals(actualTransactionCompleteResponseDTO.amountRepeat(), transaction.getAmountRepeat());
        Assertions.assertEquals(actualTransactionCompleteResponseDTO.transactionType(), transaction.getTransactionType());
        Assertions.assertEquals(actualTransactionCompleteResponseDTO.categories().size(), transaction.getCategories().size());
    }

    @Test
    void testFindById_GivenANotExistingId_ShouldThrowsATransactionNotFoundException() {
        Mockito.when(transactionRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.empty());

        TransactionNotFoundException transactionNotFoundException = Assertions.assertThrows(TransactionNotFoundException.class, () -> transactionService.findById(id));

        Assertions.assertEquals(expectedTransactionNotFoundExceptionMessage, transactionNotFoundException.getMessage());
    }

    @Test
    void testUpdate_GivenAValidIdAndTransactionCreateRequestDTO_ShouldReturnATransactionCreateResponseDTO() {
        TransactionCreateRequestDTO transactionUpdate = new TransactionCreateRequestDTO(
                "Other count",
                "",
                345.12,
                0,
                TransactionType.PAYMENT,
                new User(),
                List.of());
        Transaction transactionUpdated = new TransactionBuilder(TransactionType.PAYMENT)
                .addId(id)
                .addName("Other count")
                .addDescription("")
                .addValue(345.12)
                .addAmountRepeat(0)
                .addCreatedDate(LocalDate.now())
                .addUser(new User())
                .addCategories(List.of())
                .build();
        Mockito.when(transactionRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(transaction));
        Mockito.when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(transactionUpdated);

        TransactionCreateResponseDTO actualTransactionCreateResponseDTO = Assertions.assertDoesNotThrow(() -> transactionService.update(transactionUpdate, id));

        Assertions.assertNotNull(actualTransactionCreateResponseDTO);
        Assertions.assertEquals(transaction.getId(), actualTransactionCreateResponseDTO.id());
        Assertions.assertEquals(transactionUpdate.name(), actualTransactionCreateResponseDTO.name());
        Assertions.assertEquals(transactionUpdate.description(), actualTransactionCreateResponseDTO.description());
    }

    @Test
    void testUpdate_GivenANotValidId_ShouldThrowsATransactionNotFoundException() {
        TransactionCreateRequestDTO transactionUpdate = new TransactionCreateRequestDTO(
                "Other count",
                "",
                345.12,
                0,
                TransactionType.PAYMENT,
                new User(),
                List.of());
        Mockito.when(transactionRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.empty());

        TransactionNotFoundException actualTransactionNotFoundException = Assertions.assertThrows(TransactionNotFoundException.class, () -> transactionService.update(transactionUpdate, id));

        Assertions.assertEquals(expectedTransactionNotFoundExceptionMessage, actualTransactionNotFoundException.getMessage());
    }

    @Test
    void testDelete_GivenAnExistingId_ShouldDeleteTransaction() {
        Mockito.when(transactionRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(transaction));
        Mockito.doNothing().when(transactionRepository).delete(Mockito.any(Transaction.class));

        Assertions.assertDoesNotThrow(() -> transactionService.delete(id));

        Mockito.verify(transactionRepository, Mockito.times(1)).delete(Mockito.any(Transaction.class));
    }

    @Test
    void testDelete_GivenANotExistingId_ShouldThrowsATransactionNotFoundException() {
        Mockito.when(transactionRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.empty());

        TransactionNotFoundException actualTransactionNotFoundException = Assertions.assertThrows(TransactionNotFoundException.class, () -> transactionService.delete(id));

        Mockito.verify(transactionRepository, Mockito.never()).delete(Mockito.any(Transaction.class));
        Assertions.assertEquals(expectedTransactionNotFoundExceptionMessage, actualTransactionNotFoundException.getMessage());
    }
}
