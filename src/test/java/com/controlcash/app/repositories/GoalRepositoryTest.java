package com.controlcash.app.repositories;

import com.controlcash.app.models.Goal;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@DataJpaTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class GoalRepositoryTest {

    @Autowired
    private GoalRepository goalRepository;

    private Goal goal;

    @BeforeEach
    void setUp() throws ParseException {
        goal = new Goal();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        goal.setDueDate(dateFormat.parse("19/07/2024"));
        goal.setValue(1500.00);
    }

    @Test
    void testSave_GivenAGoalWithoutUserAndCategory_ShouldThrowsADataIntegrityViolationException() {
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> goalRepository.save(goal));
    }
}
