package com.controlcash.app.repositories;

import com.controlcash.app.models.Goal;
import com.controlcash.app.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
    void testSave_GivenAGoalWithAllAttributes_ShouldSaveAndReturnAGoal() {
        User user = new User();
        user.setUserName("user123");
        user.setEmail("user123@gmail.com");
        user.setFullName("User");
        user.setSalary(1500.00);
        user.setPassword("password");
        goal.setUser(user);

        Goal actualGoal = goalRepository.save(goal);

        Assertions.assertNotNull(actualGoal);
        Assertions.assertNotNull(actualGoal.getId());
        Assertions.assertEquals(goal.getValue(), actualGoal.getValue());
        Assertions.assertEquals(goal.getDueDate(), actualGoal.getDueDate());
        Assertions.assertEquals(goal.getUser(), actualGoal.getUser());
    }

    @Test
    void testSave_GivenAGoalWithoutUserAndCategory_ShouldThrowsADataIntegrityViolationException() {
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> goalRepository.save(goal));
    }

    @Test
    void testSave_GivenAGoalWithoutDueDate_ShouldThrowsADataIntegrityViolationException() {
        goal.setDueDate(null);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> goalRepository.save(goal));
    }
}
