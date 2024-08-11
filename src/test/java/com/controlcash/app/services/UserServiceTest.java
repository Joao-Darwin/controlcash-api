package com.controlcash.app.services;

import com.controlcash.app.dtos.user.request.UserCreateRequestDTO;
import com.controlcash.app.dtos.user.response.UserAllResponseDTO;
import com.controlcash.app.dtos.user.response.UserCompleteResponseDTO;
import com.controlcash.app.dtos.user.response.UserCreateResponseDTO;
import com.controlcash.app.exceptions.UserNotFoundException;
import com.controlcash.app.models.User;
import com.controlcash.app.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UUID id;
    private User user;
    private UserCreateRequestDTO userCreateRequestDTO;
    private String userNotFoundExceptionMessage;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        userNotFoundExceptionMessage = "User not found. Id used: " + id;
        user = new User(
                id,
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
        userCreateRequestDTO = new UserCreateRequestDTO(
                "fooBar",
                "foobar@gmail.com",
                "123456",
                "Foo Bar",
                1500.00);
    }

    @Test
    void testCreate_GivenAnUserCreateRequestDTO_ShouldSaveAndReturnAnUserCreateResponseDTO() {
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        Mockito.when(passwordEncoder.encode(Mockito.anyString()))
                .thenReturn("$2a$12$pQkRscbGtwUoX2bCqB6Ww.PTuDJlF/PnuosheRWv3jbL7nEicCc2u");

        UserCreateResponseDTO actualUserCreateResponseDTO = userService.create(userCreateRequestDTO);

        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(Mockito.anyString());
        Assertions.assertNotNull(actualUserCreateResponseDTO);
        Assertions.assertNotNull(actualUserCreateResponseDTO.id());
        Assertions.assertEquals(userCreateRequestDTO.email(), actualUserCreateResponseDTO.email());
        Assertions.assertEquals(userCreateRequestDTO.userName(), actualUserCreateResponseDTO.userName());
    }

    @Test
    void testFindAll_GivenAPageable_ShouldReturnAPageWithUserAllResponseDTO() {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "userName");
        Page<User> userPage = new PageImpl<>(List.of(user, new User()));
        Mockito.when(userRepository.findAll(Mockito.any(Pageable.class))).thenReturn(userPage);

        Page<UserAllResponseDTO> actualUserPage = userService.findAll(pageable);

        Assertions.assertNotNull(actualUserPage);
        Assertions.assertEquals(2, actualUserPage.getSize());
    }

    @Test
    void testFindAll_GivenAPageableWithoutUsers_ShouldReturnAnEmptyPage() {
        Pageable pageable = PageRequest.of(10, 10, Sort.Direction.ASC, "userName");
        Mockito.when(userRepository.findAll(Mockito.any(Pageable.class))).thenReturn(Page.empty());

        Page<UserAllResponseDTO> actualUserPage = userService.findAll(pageable);

        Assertions.assertNotNull(actualUserPage);
        Assertions.assertTrue(actualUserPage.isEmpty());
    }

    @Test
    void testFindById_GivenAValidId_ShouldReturnAnUserCompleteResponseDTO() {
        Mockito.when(userRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(user));

        UserCompleteResponseDTO actualUserCompleteResponseDTO = Assertions.assertDoesNotThrow(() -> userService.findById(id));

        Assertions.assertNotNull(actualUserCompleteResponseDTO);
        Assertions.assertEquals(id, actualUserCompleteResponseDTO.id());
        Assertions.assertEquals(user.getEmail(), actualUserCompleteResponseDTO.email());
        Assertions.assertEquals(user.getFullName(), actualUserCompleteResponseDTO.fullName());
        Assertions.assertEquals(user.getUsername(), actualUserCompleteResponseDTO.userName());
    }

    @Test
    void testFindById_GivenANotValidId_ShouldThrowsAnUserNotFoundException() {
        Mockito.when(userRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.empty());

        UserNotFoundException userNotFoundException = Assertions.assertThrows(UserNotFoundException.class, () -> userService.findById(id));

        Assertions.assertEquals(userNotFoundExceptionMessage, userNotFoundException.getMessage());
    }
}
