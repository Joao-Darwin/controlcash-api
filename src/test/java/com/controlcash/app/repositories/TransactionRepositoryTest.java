package com.controlcash.app.repositories;

import com.controlcash.app.models.Transaction;
import com.controlcash.app.models.User;
import com.controlcash.app.models.enums.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@DataJpaTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    SimpleDateFormat dateFormat;
    private Transaction transaction;
    private User user;

    @BeforeEach
    void setUp() throws ParseException {
         dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        transaction = new Transaction();
        transaction.setName("Game purchase");
        transaction.setCreatedDate(dateFormat.parse("19/07/2024"));
        transaction.setValue(250.00);
        transaction.setAmountRepeat(5);
        transaction.setTransactionType(TransactionType.PAYMENT);

        user = new User();
        user.setUserName("user123");
        user.setEmail("user123@gmail.com");
        user.setFullName("User");
        user.setSalary(1500.00);
        user.setPassword("password");
    }
}
