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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Test
    void testLoadUserByUsername_GivenAValidUsername_ShouldReturnAnUserDetails() {
        Mockito.when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.of(user));

        UserDetails userDetails = Assertions.assertDoesNotThrow(() -> userService.loadUserByUsername("fooBar"));

        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals(user.getUsername(), userDetails.getUsername());
        Assertions.assertEquals(user.getPassword(), userDetails.getPassword());
    }

    @Test
    void testLoadUserByUsername_GivenANotValidUsername_ThrowsAnUsernameNotFoundException() {
        String expectedUsername = "fooBar";
        String expectedUsernameNotFoundExceptionMessage = "Email not found. Email used: '" + expectedUsername + "'";
        Mockito.when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.empty());

        UsernameNotFoundException exception = Assertions.assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(expectedUsername));

        Assertions.assertEquals(expectedUsernameNotFoundExceptionMessage, exception.getMessage());
    }

    @Test
    void testUpdate_GivenAnUserCreateRequestDTOAndValidId_ShouldReturnAnUserCreateResponseDTOUpdated() {
        User userUpdated = new User(
                id,
                "fooBar2",
                "foobar2@gmail.com",
                "1234567",
                "Foo Bar2",
                1500.00,
                true,
                true,
                true,
                true,
                List.of(),
                List.of(),
                List.of());
        UserCreateRequestDTO userCreateRequestDTOUpdated = new UserCreateRequestDTO(
                "fooBar2",
                "foobar2@gmail.com",
                "1234567",
                "Foo Bar2",
                1500.00);
        Mockito.when(userRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(userUpdated);
        Mockito.when(passwordEncoder.encode(Mockito.anyString()))
                .thenReturn("$2a$12$pQkRscbGtwUoX2bCqB6Ww.PTuDJlF/PnuosheRWv3jbL7nEicCc2u");

        UserCreateResponseDTO actualUserResponseDTO = userService.update(id, userCreateRequestDTOUpdated);

        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(Mockito.anyString());
        Assertions.assertNotNull(actualUserResponseDTO);
        Assertions.assertNotNull(actualUserResponseDTO.id());
        Assertions.assertEquals(actualUserResponseDTO.email(), userCreateRequestDTOUpdated.email());
        Assertions.assertEquals(actualUserResponseDTO.userName(), userCreateRequestDTOUpdated.userName());
    }

    @Test
    void testUpdate_GivenAnUserCreateRequestDTOAndNotValidId_ShouldThrowsAnUserNotFoundException() {
        UserCreateRequestDTO userCreateRequestDTOUpdated = new UserCreateRequestDTO(
                "fooBar2",
                "foobar2@gmail.com",
                "1234567",
                "Foo Bar2",
                1500.00);
        Mockito.when(userRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.empty());

        UserNotFoundException userNotFoundException = Assertions.assertThrows(UserNotFoundException.class, () -> userService.update(id, userCreateRequestDTOUpdated));

        Assertions.assertEquals(userNotFoundExceptionMessage, userNotFoundException.getMessage());
    }

    @Test
    void testDelete_GivenAValidId_ShouldDeleteUser() {
        Mockito.when(userRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(user));

        Assertions.assertDoesNotThrow(() -> userService.delete(id));

        Mockito.verify(userRepository, Mockito.times(1)).delete(Mockito.any(User.class));
    }

    @Test
    void testDelete_GivenANotValidId_ShouldThrowsAnUserNotFoundException() {
        Mockito.when(userRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.empty());

        UserNotFoundException userNotFoundException = Assertions.assertThrows(UserNotFoundException.class, () -> userService.delete(id));

        Assertions.assertEquals(userNotFoundExceptionMessage, userNotFoundException.getMessage());
    }
}
