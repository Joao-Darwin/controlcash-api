package com.controlcash.app.repositories;

import com.controlcash.app.models.Transaction;
import com.controlcash.app.models.User;
import com.controlcash.app.models.enums.TransactionType;
import com.controlcash.app.utils.dates.DateFormatUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    private Transaction transaction;
    private User user;
    private DateFormatUtils dateFormatUtils;

    @BeforeEach
    void setUp() throws ParseException {
        dateFormatUtils = DateFormatUtils.getInstance();

        user = new User();
        user.setUserName("user123");
        user.setEmail("user123@gmail.com");
        user.setFullName("User");
        user.setSalary(1500.00);
        user.setPassword("password");

        transaction = new Transaction();
        transaction.setName("Game purchase");
        transaction.setCreatedDate(dateFormatUtils.convertStringToDate("19/07/2024"));
        transaction.setValue(250.00);
        transaction.setAmountRepeat(5);
        transaction.setTransactionType(TransactionType.PAYMENT);
        transaction.setUser(user);
    }

    @Test
    void testSave_GivenATransactionWithOnlyMandatoryAttributes_ShouldSaveAndReturnATransaction() {
        Transaction actualTransaction = transactionRepository.save(transaction);

        Assertions.assertNotNull(actualTransaction);
        Assertions.assertNotNull(actualTransaction.getId());
        Assertions.assertEquals(transaction.getAmountRepeat(), actualTransaction.getAmountRepeat());
        Assertions.assertEquals(transaction.getName(), actualTransaction.getName());
        Assertions.assertEquals(transaction.getTransactionType(), actualTransaction.getTransactionType());
        Assertions.assertEquals(transaction.getValue(), actualTransaction.getValue());
        Assertions.assertEquals(transaction.getCreatedDate(), actualTransaction.getCreatedDate());
        Assertions.assertEquals(transaction.getUser(), actualTransaction.getUser());
    }

    @Test
    void testSave_GivenATransactionWithAllAttributes_ShouldSaveAndReturnATransaction() {
        transaction.setDescription("The game was Assassin's Creed Origins");
        transaction.setCategories(List.of());

        Transaction actualTransaction = transactionRepository.save(transaction);

        Assertions.assertNotNull(actualTransaction);
        Assertions.assertNotNull(actualTransaction.getId());
        Assertions.assertEquals(transaction.getAmountRepeat(), actualTransaction.getAmountRepeat());
        Assertions.assertEquals(transaction.getDescription(), actualTransaction.getDescription());
        Assertions.assertEquals(transaction.getName(), actualTransaction.getName());
        Assertions.assertEquals(transaction.getTransactionType(), actualTransaction.getTransactionType());
        Assertions.assertEquals(transaction.getValue(), actualTransaction.getValue());
        Assertions.assertEquals(transaction.getCreatedDate(), actualTransaction.getCreatedDate());
        Assertions.assertEquals(transaction.getUser(), actualTransaction.getUser());
        Assertions.assertEquals(transaction.getCategories(), actualTransaction.getCategories());
    }

    @Test
    void testSave_WhenNameIsNull_ShouldThrowsADataIntegrityViolationException() {
        transaction.setName(null);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> transactionRepository.save(transaction));
    }

    @Test
    void testSave_WhenCreatedDateIsNull_ShouldThrowsADataIntegrityViolationException() {
        transaction.setCreatedDate(null);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> transactionRepository.save(transaction));
    }

    @Test
    void testSave_WhenValueIsNull_ShouldThrowsADataIntegrityViolationException() {
        transaction.setValue(null);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> transactionRepository.save(transaction));
    }

    @Test
    void testSave_WhenValueIsLessThanZero_ShouldThrowsADataIntegrityViolationException() {
        transaction.setValue(-250.00);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> transactionRepository.saveAndFlush(transaction));
    }

    @Test
    void testSave_WhenAmountRepeatIsNull_ShouldThrowsADataIntegrityViolationException() {
        transaction.setAmountRepeat(null);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> transactionRepository.save(transaction));
    }

    @Test
    void testSave_WhenAmountRepeatIsLessThanZero_ShouldThrowsADataIntegrityViolationException() {
        transaction.setAmountRepeat(-1);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> transactionRepository.saveAndFlush(transaction));
    }

    @Test
    void testSave_WhenUserIsNull_ShouldThrowsADataIntegrityViolationException() {
        transaction.setUser(null);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> transactionRepository.save(transaction));
    }

    @Test
    void testFindAll_ShouldReturnATransactionList() throws ParseException {
        transactionRepository.save(transaction);
        transaction = new Transaction();
        transaction.setName("Freelancer");
        transaction.setValue(2500.00);
        transaction.setTransactionType(TransactionType.ENTRANCE);
        transaction.setCreatedDate(dateFormatUtils.convertStringToDate("19/07/2024"));
        transaction.setAmountRepeat(5);
        transaction.setUser(user);
        transactionRepository.save(transaction);

        List<Transaction> actualTransactions = transactionRepository.findAll();

        Assertions.assertNotNull(actualTransactions);
        Assertions.assertEquals(2, actualTransactions.size());
    }

    @Test
    void testFindAll_GivenAPageable_ShouldReturnATransactionPage() throws ParseException {
        transactionRepository.save(transaction);
        transaction = new Transaction();
        transaction.setName("Freelancer");
        transaction.setValue(2500.00);
        transaction.setTransactionType(TransactionType.ENTRANCE);
        transaction.setCreatedDate(dateFormatUtils.convertStringToDate("19/07/2024"));
        transaction.setAmountRepeat(5);
        transaction.setUser(user);
        transactionRepository.save(transaction);
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "name"));

        Page<Transaction> transactionPage = transactionRepository.findAll(pageable);

        Assertions.assertNotNull(transactionPage);
        Assertions.assertTrue(transactionPage.hasContent());
    }

    @Test
    void testFindById_GivenAValidId_ShouldReturnAPresentOptionalTransaction() {
        transaction = transactionRepository.save(transaction);
        UUID expectedId = transaction.getId();

        Optional<Transaction> optionalTransaction = transactionRepository.findById(expectedId);

        Assertions.assertTrue(optionalTransaction.isPresent());
        Transaction actualTransaction = optionalTransaction.get();
        Assertions.assertEquals(expectedId, actualTransaction.getId());
    }

    @Test
    void testFindById_GivenANotValidId_ShouldReturnAEmptyOptionalTransaction() {
        UUID id = UUID.randomUUID();

        Optional<Transaction> optionalTransaction = transactionRepository.findById(id);

        Assertions.assertTrue(optionalTransaction.isEmpty());
    }

    @Test
    void testUpdate_GivenATransactionWithId_ShouldUpdateTransaction() {
        Double expectedNewValue = 345.00;
        transaction = transactionRepository.save(transaction);

        transaction.setValue(expectedNewValue);
        transactionRepository.save(transaction);

        Optional<Transaction> optionalTransaction = transactionRepository.findById(transaction.getId());
        Assertions.assertTrue(optionalTransaction.isPresent());
        Transaction updatedTransaction = optionalTransaction.get();
        Assertions.assertEquals(transaction.getId(), updatedTransaction.getId());
        Assertions.assertEquals(expectedNewValue, updatedTransaction.getValue());
    }
}
