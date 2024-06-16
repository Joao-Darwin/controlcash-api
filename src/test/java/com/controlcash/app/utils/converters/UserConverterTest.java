package com.controlcash.app.utils.converters;

import com.controlcash.app.dtos.request.UserCreateRequestDTO;
import com.controlcash.app.dtos.response.UserAllResponseDTO;
import com.controlcash.app.dtos.response.UserCreateResponseDTO;
import com.controlcash.app.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserConverterTest {

    @Test
    public void testConvertUserCreateRequestDTOToUser_WhenUserCreateRequestDTOIsPassed_ShouldReturnUser() {
        String expectedUserName = "joodarwin";
        String expectedEmail = "joaodarwin@email.com";
        String expectedPassword = "123456";
        String expectedFullName = "João Darwin";
        Double expectedSalary = 3900.00;
        boolean expectedAccountNonExpired = false;
        boolean expectedAccountNonLocked = false;
        boolean expectedCredentialsNonExpired = false;
        boolean expectedEnabled = false;
        UserCreateRequestDTO userCreateRequestDTO = new UserCreateRequestDTO(expectedUserName, expectedEmail, expectedPassword, expectedFullName, expectedSalary);

        User user = UserConverter.convertUserCreateRequestDTOToUser(userCreateRequestDTO);

        Assertions.assertEquals(expectedUserName, user.getUsername());
        Assertions.assertEquals(expectedEmail, user.getEmail());
        Assertions.assertEquals(expectedPassword, user.getPassword());
        Assertions.assertEquals(expectedFullName, user.getFullName());
        Assertions.assertEquals(expectedSalary, user.getSalary());
        Assertions.assertEquals(expectedAccountNonExpired, user.isAccountNonExpired());
        Assertions.assertEquals(expectedAccountNonLocked, user.isAccountNonLocked());
        Assertions.assertEquals(expectedCredentialsNonExpired, user.isCredentialsNonExpired());
        Assertions.assertEquals(expectedEnabled, user.isEnabled());
    }

    @Test
    public void testConvertUserCreateRequestDTOToUser_WhenNullPassed_ShouldThrowsANullPointerException() {
        UserCreateRequestDTO userCreateRequestDTO = null;

        Assertions.assertThrows(NullPointerException.class, () -> UserConverter.convertUserCreateRequestDTOToUser(userCreateRequestDTO));
    }

    @Test
    public void testConvertUserToUserCreateResponseDTO_WhenUserIsPassed_ShouldReturnUserCreateRequestDTO() {
        String expectedUserName = "joodarwin";
        String expectedEmail = "joaodarwin@email.com";
        User user = new User();
        user.setUserName(expectedUserName);
        user.setEmail(expectedEmail);

        UserCreateResponseDTO userCreateResponseDTO = UserConverter.convertUserToUserCreateResponseDTO(user);

        Assertions.assertEquals(expectedUserName, userCreateResponseDTO.userName());
        Assertions.assertEquals(expectedEmail, userCreateResponseDTO.email());
    }

    @Test
    public void testConvertUserToUserAllResponseDTO_WhenUserIsPassed_ShouldReturnUserCreateRequestDTO() {
        UUID expectedId = UUID.randomUUID();
        String expectedUserName = "joodarwin";
        String expectedEmail = "joaodarwin@email.com";
        User user = new User();
        user.setId(expectedId);
        user.setUserName(expectedUserName);
        user.setEmail(expectedEmail);

        UserAllResponseDTO userCreateResponseDTO = UserConverter.convertUserToUserAllResponseDTO(user);

        Assertions.assertEquals(expectedId, userCreateResponseDTO.id());
        Assertions.assertEquals(expectedUserName, userCreateResponseDTO.userName());
        Assertions.assertEquals(expectedEmail, userCreateResponseDTO.email());
    }
}
