package com.controlcash.app.services;

import com.controlcash.app.dtos.permission.request.PermissionCreateRequestDTO;
import com.controlcash.app.dtos.permission.request.PermissionUpdateRequestDTO;
import com.controlcash.app.dtos.permission.response.AllPermissionResponseDTO;
import com.controlcash.app.dtos.permission.response.PermissionResponseDTO;
import com.controlcash.app.exceptions.PermissionNotFoundException;
import com.controlcash.app.models.Permission;
import com.controlcash.app.repositories.PermissionRepository;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class PermissionServiceTest {

    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private PermissionService permissionService;

    private String expectedPermissionNotFoundExceptionMessage;
    private UUID id;
    private Permission permission;
    private PermissionCreateRequestDTO permissionCreateRequestDTO;

    @BeforeEach
    void setUp() {
        String description = "Admin";
        id = UUID.randomUUID();
        expectedPermissionNotFoundExceptionMessage = "Permission not found. Id used: " + id;
        permission = new Permission(id, description, List.of());
        permissionCreateRequestDTO = new PermissionCreateRequestDTO(description);
    }

    @Test
    void testCreate_GivenAPermissionCreateRequestDTO_ShouldSaveAndReturnAPermissionResponseDTO() {
        Mockito.when(permissionRepository.save(Mockito.any(Permission.class))).thenReturn(permission);

        PermissionResponseDTO actualPermissionResponseDTO = permissionService.create(permissionCreateRequestDTO);

        Assertions.assertNotNull(actualPermissionResponseDTO);
        Assertions.assertNotNull(actualPermissionResponseDTO.id());
        Assertions.assertEquals(permissionCreateRequestDTO.description(), actualPermissionResponseDTO.description());
    }

    @Test
    void testFindAll_GivenAPageable_ShouldReturnAPageWithAllPermissionResponseDTO() {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "description");
        List<Permission> allPermissionList = List.of(permission, new Permission(UUID.randomUUID(), "User", List.of()));
        Page<Permission> permissionPage = new PageImpl<>(allPermissionList);
        Mockito.when(permissionRepository.findAll(Mockito.any(Pageable.class))).thenReturn(permissionPage);

        Page<AllPermissionResponseDTO> actualAllPermissionResponseDTOS = permissionService.findAll(pageable);

        Assertions.assertNotNull(actualAllPermissionResponseDTOS);
        Assertions.assertEquals(2, actualAllPermissionResponseDTOS.getSize());
    }

    @Test
    void testFindAll_GivenAPageableWithNotValues_ShouldReturnAPageWithAllPermissionResponseDTOEmpty() {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "description");
        Page<Permission> permissionPage = Page.empty();
        Mockito.when(permissionRepository.findAll(Mockito.any(Pageable.class))).thenReturn(permissionPage);

        Page<AllPermissionResponseDTO> actualAllPermissionResponseDTOS = permissionService.findAll(pageable);

        Assertions.assertNotNull(actualAllPermissionResponseDTOS);
        Assertions.assertTrue(actualAllPermissionResponseDTOS.isEmpty());
    }

    @Test
    void testFindById_GivenAValidId_ShouldReturnAPermissionResponseDTO() {
        Mockito.when(permissionRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(permission));

        PermissionResponseDTO actualPermissionResponseDTO = Assertions.assertDoesNotThrow(() -> permissionService.findById(id));

        Assertions.assertNotNull(actualPermissionResponseDTO);
        Assertions.assertEquals(id, actualPermissionResponseDTO.id());
        Assertions.assertEquals(permission.getAuthority(), actualPermissionResponseDTO.description());
    }

    @Test
    void testFindById_GivenANotValidId_ShouldThrowsAPermissionNotFoundException() {
        Mockito.when(permissionRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.empty());

        PermissionNotFoundException permissionNotFoundException = Assertions.assertThrows(PermissionNotFoundException.class, () -> permissionService.findById(id));

        Assertions.assertEquals(expectedPermissionNotFoundExceptionMessage, permissionNotFoundException.getMessage());
    }

    @Test
    void testUpdate_GivenAPermissionUpdateRequestDTOAndValidId_ShouldReturnAPermissionResponseDTOUpdated() {
        PermissionUpdateRequestDTO permissionUpdateRequestDTO = new PermissionUpdateRequestDTO("Admin", List.of());
        Permission permissionUpdated = new Permission(id, "Admin", List.of());
        Mockito.when(permissionRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(permission));
        Mockito.when(permissionRepository.save(Mockito.any(Permission.class))).thenReturn(permissionUpdated);

        PermissionResponseDTO actualPermissionResponseDTO = Assertions.assertDoesNotThrow(() -> permissionService.update(permissionUpdateRequestDTO, id));

        Assertions.assertNotNull(actualPermissionResponseDTO);
        Assertions.assertEquals(id, actualPermissionResponseDTO.id());
        Assertions.assertEquals(permissionUpdateRequestDTO.description(), actualPermissionResponseDTO.description());
    }
}
