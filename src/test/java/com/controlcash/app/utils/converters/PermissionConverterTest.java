package com.controlcash.app.utils.converters;

import com.controlcash.app.dtos.permission.request.PermissionCreateRequestDTO;
import com.controlcash.app.models.Permission;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

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
}
