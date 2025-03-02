package com.controlcash.app.repositories;

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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest
@TestMethodOrder(MethodOrderer.MethodName.class)
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
        Assertions.assertEquals(user.isEnabled(), actualUser.isEnabled());
        Assertions.assertEquals(user.isAccountNonExpired(), actualUser.isAccountNonExpired());
        Assertions.assertEquals(user.isAccountNonLocked(), actualUser.isAccountNonLocked());
        Assertions.assertEquals(user.isCredentialsNonExpired(), actualUser.isCredentialsNonExpired());
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
        anotherUser.setPassword("another password");
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
        anotherUser.setPassword("another password");

        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            userRepository.save(anotherUser);
            entityManager.flush();
        });
    }

    @Test
    void testFindAll_ShouldReturnUserList() {
        userRepository.save(user);
        User anotherUser = new User();
        anotherUser.setUserName("user02");
        anotherUser.setEmail("user02@gmail.com");
        anotherUser.setFullName("Another User");
        anotherUser.setSalary(1500.00);
        anotherUser.setPassword("another password");
        userRepository.save(anotherUser);

        List<User> userList = userRepository.findAll();

        Assertions.assertNotNull(userList);
        Assertions.assertEquals(2, userList.size());
    }

    @Test
    void testFindById_GivenValidId_ShouldReturnUser() {
        User expectedUser = userRepository.save(user);

        Optional<User> userOptional = userRepository.findById(expectedUser.getId());

        Assertions.assertTrue(userOptional.isPresent());
        User actualUser = userOptional.get();
        Assertions.assertEquals(expectedUser.getId(), actualUser.getId());
        Assertions.assertEquals(expectedUser.getUsername(), actualUser.getUsername());
        Assertions.assertEquals(expectedUser.getPassword(), actualUser.getPassword());
        Assertions.assertEquals(expectedUser.getSalary(), actualUser.getSalary());
        Assertions.assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        Assertions.assertEquals(expectedUser.getFullName(), actualUser.getFullName());
    }

    @Test
    void testFindById_GivenNotValidId_ShouldReturnAnOptionalEmpty() {
        UUID expectedId = UUID.randomUUID();

        Optional<User> userOptional = userRepository.findById(expectedId);

        Assertions.assertTrue(userOptional.isEmpty());
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

    @Test
    void testFindByEmail_GivenValidEmail_ShouldReturnAUser() {
        userRepository.save(user);

        Optional<User> optionalUser = userRepository.findByEmail("user123@gmail.com");

        Assertions.assertTrue(optionalUser.isPresent());

        User actualUser = optionalUser.get();

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
    void testUpdateUser_ShouldReturnTheUpdatedUser() {
        user = userRepository.save(user);
        user.setFullName("Another full name");

        User actualUser = userRepository.save(user);

        Assertions.assertEquals(user.getFullName(), actualUser.getFullName());
    }

    @Test
    void testDeleteById_GivenUserId_ShouldRemoveUser(){
        user = userRepository.save(user);

        userRepository.deleteById(user.getId());
        Optional<User> optionalUser = userRepository.findById(user.getId());

        Assertions.assertTrue(optionalUser.isEmpty());
    }
}
