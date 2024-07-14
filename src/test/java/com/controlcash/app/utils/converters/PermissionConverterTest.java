package com.controlcash.app.utils.converters;

import com.controlcash.app.dtos.permission.request.PermissionCreateRequestDTO;
import com.controlcash.app.dtos.permission.response.AllPermissionResponseDTO;
import com.controlcash.app.dtos.permission.response.PermissionResponseDTO;
import com.controlcash.app.models.Permission;
import com.controlcash.app.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PermissionConverterTest {

    @Test
    void testConvertPermissionCreateRequestDTOToPermission_ShouldReturnAPermission() {
        String expectedDescriptionPermission = "ADMIN";
        PermissionCreateRequestDTO permissionCreateRequestDTO = new PermissionCreateRequestDTO(expectedDescriptionPermission);

        Permission permission = PermissionConverter.convertPermissionCreateRequestDTOToPermission(permissionCreateRequestDTO);

        Assertions.assertNull(permission.getId());
        Assertions.assertNull(permission.getUsers());
        Assertions.assertEquals(expectedDescriptionPermission, permission.getAuthority());
    }

    @Test
    void testConvertPermissionToPermissionResponseDTO_ShouldReturnAPermissionResponseDTO() {
        List<User> userList = List.of();
        UUID expectedId = UUID.randomUUID();
        String expectedDescription = "ADMIN";
        Permission permission = Mockito.mock(Permission.class);
        Mockito.when(permission.getId()).thenReturn(expectedId);
        Mockito.when(permission.getAuthority()).thenReturn(expectedDescription);
        Mockito.when(permission.getUsers()).thenReturn(userList);

        PermissionResponseDTO permissionResponseDTO = PermissionConverter.convertPermissionToPermissionResponseDTO(permission);

        Assertions.assertEquals(expectedId, permissionResponseDTO.id());
        Assertions.assertEquals(expectedDescription, permissionResponseDTO.description());
        Assertions.assertEquals(expectedId, permissionResponseDTO.id());
    }

    @Test
    void testConvertPermissionToAllPermissionResponseDTO_ShouldReturnAPermissionResponseDTO() {
        UUID expectedId = UUID.randomUUID();
        String expectedDescription = "ADMIN";
        Permission permission = Mockito.mock(Permission.class);
        Mockito.when(permission.getId()).thenReturn(expectedId);
        Mockito.when(permission.getAuthority()).thenReturn(expectedDescription);

        AllPermissionResponseDTO allPermissionResponseDTO = PermissionConverter.convertPermissionToAllPermissionResponseDTO(permission);

        Assertions.assertEquals(expectedId, allPermissionResponseDTO.id());
        Assertions.assertEquals(expectedDescription, allPermissionResponseDTO.description());
    }
}
