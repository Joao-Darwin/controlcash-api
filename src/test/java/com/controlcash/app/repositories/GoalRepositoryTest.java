package com.controlcash.app.repositories;

import com.controlcash.app.models.Category;
import com.controlcash.app.models.Goal;
import com.controlcash.app.models.User;
import jakarta.persistence.EntityManager;
import org.hibernate.exception.ConstraintViolationException;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class GoalRepositoryTest {

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private EntityManager entityManager;

    SimpleDateFormat dateFormat;

    private Goal goal;
    private User user;

    @BeforeEach
    void setUp() throws ParseException {
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        goal = new Goal();
        goal.setDueDate(dateFormat.parse("19/07/2024"));
        goal.setValue(1500.00);

        user = new User();
        user.setUserName("user123");
        user.setEmail("user123@gmail.com");
        user.setFullName("User");
        user.setSalary(1500.00);
        user.setPassword("password");
    }

    @Test
    void testSave_GivenAGoalWithAllAttributes_ShouldSaveAndReturnAGoal() {
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

    @Test
    void testSave_GivenAGoalWithValueSmallerThanZero_ShouldThrowsAConstraintViolationException() {
        goal.setUser(user);
        goal.setValue(-250.00);

        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            goalRepository.save(goal);
            entityManager.flush();
        });
    }

    @Test
    void testSave_GivenAGoalWithValueEqualsZero_ShouldThrowsAConstraintViolationException() {
        goal.setUser(user);
        goal.setValue(0.00);

        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            goalRepository.save(goal);
            entityManager.flush();
        });
    }

    @Test
    void testFindAll_ShouldReturnAListWithAllGoals() throws ParseException {
        goal.setUser(user);
        goalRepository.save(goal);
        goal = new Goal();
        goal.setDueDate(dateFormat.parse("19/07/2024"));
        goal.setValue(250.0);
        goal.setUser(user);
        goalRepository.save(goal);

        List<Goal> goals = goalRepository.findAll();

        Assertions.assertNotNull(goals);
        Assertions.assertEquals(2, goals.size());
    }

    @Test
    void testFindById_GivenAnIdValid_ShouldReturnAGoal() {
        goal.setUser(user);
        goal = goalRepository.save(goal);
        UUID expectedId = goal.getId();

        Optional<Goal> optionalGoal = goalRepository.findById(expectedId);

        Assertions.assertTrue(optionalGoal.isPresent());
        Goal actualGoal = optionalGoal.get();
        Assertions.assertEquals(goal.getValue(), actualGoal.getValue());
        Assertions.assertEquals(goal.getDueDate(), actualGoal.getDueDate());
    }

    @Test
    void testFindById_GivenAnIdNotValid_ShouldReturnAOptionalEmpty() {
        UUID uuid = UUID.randomUUID();

        Optional<Goal> optionalGoal = goalRepository.findById(uuid);

        Assertions.assertTrue(optionalGoal.isEmpty());
    }
}
