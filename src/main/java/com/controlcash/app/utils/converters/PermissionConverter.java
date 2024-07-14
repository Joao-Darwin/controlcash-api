package com.controlcash.app.utils.converters;

import com.controlcash.app.dtos.permission.request.PermissionCreateRequestDTO;
import com.controlcash.app.models.Permission;

public class PermissionConverter {

    public static Permission convertPermissionCreateRequestDTOToPermission(PermissionCreateRequestDTO permissionCreateRequestDTO) {
        Permission permission = new Permission();

        permission.setDescription(permissionCreateRequestDTO.description());
        permission.setUsers(null);

        return permission;
    }
}
