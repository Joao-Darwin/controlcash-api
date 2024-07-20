package com.controlcash.app.repositories;

import com.controlcash.app.models.Goal;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
    void testSave_GivenAGoalWithoutUserAndCategory_ShouldSaveAndReturnAGoal() {
        Goal actualGoal = goalRepository.save(goal);

        Assertions.assertNotNull(actualGoal);
        Assertions.assertNotNull(actualGoal.getId());
        Assertions.assertEquals(goal.getValue(), actualGoal.getValue());
        Assertions.assertEquals(goal.getDueDate(), actualGoal.getDueDate());
        Assertions.assertNull(actualGoal.getUser());
        Assertions.assertNull(actualGoal.getCategory());
    }
}
