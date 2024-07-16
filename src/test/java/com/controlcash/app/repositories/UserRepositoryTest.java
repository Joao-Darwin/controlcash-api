package com.controlcash.app.repositories;

import com.controlcash.app.models.User;
import jakarta.persistence.EntityManager;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserName("user123");
        user.setEmail("user123@gmail.com");
        user.setFullName("User");
        user.setSalary(1500.00);
        user.setPassword("password");
    }

    @Test
    void testSave_GivenUserWithAllAttributes_ShouldSaveAndReturnAUser() {
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);

        User actualUser = userRepository.save(user);

        Assertions.assertNotNull(actualUser.getId());
        Assertions.assertEquals(user.getUsername(), actualUser.getUsername());
        Assertions.assertEquals(user.getPassword(), actualUser.getPassword());
        Assertions.assertEquals(user.getSalary(), actualUser.getSalary());
        Assertions.assertEquals(user.getEmail(), actualUser.getEmail());
        Assertions.assertEquals(user.getFullName(), actualUser.getFullName());
        Assertions.assertTrue(actualUser.isEnabled());
        Assertions.assertTrue(actualUser.isAccountNonExpired());
        Assertions.assertTrue(actualUser.isAccountNonLocked());
        Assertions.assertTrue(actualUser.isCredentialsNonExpired());
    }

    @Test
    void testSave_GivenUserWithoutAllAttributes_ShouldSaveAndReturnAUser() {
        User actualUser = userRepository.save(user);

        Assertions.assertNotNull(actualUser.getId());
        Assertions.assertEquals(user.getUsername(), actualUser.getUsername());
        Assertions.assertEquals(user.getPassword(), actualUser.getPassword());
        Assertions.assertEquals(user.getSalary(), actualUser.getSalary());
        Assertions.assertEquals(user.getEmail(), actualUser.getEmail());
        Assertions.assertEquals(user.getFullName(), actualUser.getFullName());
        Assertions.assertFalse(actualUser.isEnabled());
        Assertions.assertFalse(actualUser.isAccountNonExpired());
        Assertions.assertFalse(actualUser.isAccountNonLocked());
        Assertions.assertFalse(actualUser.isCredentialsNonExpired());
    }

    @Test
    void testSave_WhenViolateUsernameUniqueConstraint_ShouldThrowsAConstraintViolationException() {
        userRepository.save(user);
        User anotherUser = new User();
        anotherUser.setUserName("user123");
        anotherUser.setEmail("anotheruser@gmail.com");
        anotherUser.setFullName("Another User");
        anotherUser.setSalary(1500.00);
        anotherUser.setPassword("anotherpassword");
        userRepository.save(anotherUser);

        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            userRepository.save(anotherUser);
            entityManager.flush();
        });
    }

    @Test
    void testSave_WhenViolateEmailUniqueConstraint_ShouldThrowsAConstraintViolationException() {
        userRepository.save(user);
        User anotherUser = new User();
        anotherUser.setUserName("anotherUser");
        anotherUser.setEmail("user123@gmail.com");
        anotherUser.setFullName("Another User");
        anotherUser.setSalary(1500.00);
        anotherUser.setPassword("anotherpassword");

        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            userRepository.save(anotherUser);
            entityManager.flush();
        });
    }

    @Test
    void testFindByUsername_GivenUsername_ShouldReturnAUser() {
        userRepository.save(user);

        User actualUser = userRepository.findByUsername("user123");

        Assertions.assertNotNull(actualUser.getId());
        Assertions.assertEquals(user.getUsername(), actualUser.getUsername());
        Assertions.assertEquals(user.getPassword(), actualUser.getPassword());
        Assertions.assertEquals(user.getSalary(), actualUser.getSalary());
        Assertions.assertEquals(user.getEmail(), actualUser.getEmail());
        Assertions.assertEquals(user.getFullName(), actualUser.getFullName());
        Assertions.assertFalse(actualUser.isEnabled());
        Assertions.assertFalse(actualUser.isAccountNonExpired());
        Assertions.assertFalse(actualUser.isAccountNonLocked());
        Assertions.assertFalse(actualUser.isCredentialsNonExpired());
    }
}
