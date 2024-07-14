package com.controlcash.app.utils.converters;

import com.controlcash.app.dtos.permission.request.PermissionCreateRequestDTO;
import com.controlcash.app.dtos.permission.response.PermissionResponseDTO;
import com.controlcash.app.dtos.user.response.UserAllResponseDTO;
import com.controlcash.app.models.Permission;

import java.util.List;

public class PermissionConverter {

    public static Permission convertPermissionCreateRequestDTOToPermission(PermissionCreateRequestDTO permissionCreateRequestDTO) {
        Permission permission = new Permission();

        permission.setDescription(permissionCreateRequestDTO.description());
        permission.setUsers(null);

        return permission;
    }

    public static PermissionResponseDTO convertPermissionToPermissionResponseDTO(Permission permission) {
        final List<UserAllResponseDTO> userAllResponseDTOList = permission
                .getUsers()
                .stream()
                .map(UserConverter::convertUserToUserAllResponseDTO)
                .toList();

        return new PermissionResponseDTO(permission.getId(), permission.getAuthority(), userAllResponseDTOList);
    }
}
