package com.controlcash.app.services;

import com.controlcash.app.dtos.user.request.UserCreateRequestDTO;
import com.controlcash.app.dtos.user.response.UserCreateResponseDTO;
import com.controlcash.app.models.User;
import com.controlcash.app.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UUID id;
    private User user;
    private UserCreateRequestDTO userCreateRequestDTO;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        user = new User(id,
                "fooBar",
                "foobar@gmail.com",
                "123456",
                "Foo Bar",
                1500.00,
                true,
                true,
                true,
                true,
                List.of(),
                List.of(),
                List.of());
        userCreateRequestDTO = new UserCreateRequestDTO("fooBar", "foobar@gmail.com", "123456", "Foo Bar", 1500.00);
    }

    @Test
    void testCreate_GivenAnUserCreateRequestDTO_ShouldSaveAndReturnAnUserCreateResponseDTO() {
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        UserCreateResponseDTO actualUserCreateResponseDTO = userService.create(userCreateRequestDTO);

        Assertions.assertNotNull(actualUserCreateResponseDTO);
        Assertions.assertNotNull(actualUserCreateResponseDTO.id());
        Assertions.assertEquals(userCreateRequestDTO.email(), actualUserCreateResponseDTO.email());
        Assertions.assertEquals(userCreateRequestDTO.userName(), actualUserCreateResponseDTO.userName());
    }
}
