package com.controlcash.app.services;

import com.controlcash.app.dtos.permission.request.PermissionCreateRequestDTO;
import com.controlcash.app.dtos.permission.response.PermissionResponseDTO;
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

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class PermissionServiceTest {

    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private PermissionService permissionService;

    private UUID id;
    private Permission permission;
    private PermissionCreateRequestDTO permissionCreateRequestDTO;

    @BeforeEach
    void setUp() {
        String description = "Admin";
        id = UUID.randomUUID();
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
}
