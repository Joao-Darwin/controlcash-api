package com.controlcash.app.repositories;

import com.controlcash.app.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByUsername_GivenUsername_ShouldReturnAUser() {
        String expectedUserName = "user123";
        User user = new User();
        user.setUserName(expectedUserName);
        user.setEmail("user123@gmail.com");
        user.setFullName("User");
        user.setSalary(1500.00);
        user.setPassword("password");
        userRepository.save(user);

        User actualUser = userRepository.findByUsername(expectedUserName);

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
