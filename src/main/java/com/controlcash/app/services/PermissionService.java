package com.controlcash.app.services;

import com.controlcash.app.dtos.permission.request.PermissionCreateRequestDTO;
import com.controlcash.app.dtos.permission.response.AllPermissionResponseDTO;
import com.controlcash.app.dtos.permission.response.PermissionResponseDTO;
import com.controlcash.app.exceptions.PermissionNotFoundException;
import com.controlcash.app.exceptions.UserNotFoundException;
import com.controlcash.app.models.Permission;
import com.controlcash.app.models.User;
import com.controlcash.app.repositories.PermissionRepository;
import com.controlcash.app.utils.converters.PermissionConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    @Autowired
    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public PermissionResponseDTO create(PermissionCreateRequestDTO permissionCreateRequestDTO) {
        Permission permission = PermissionConverter.convertPermissionCreateRequestDTOToPermission(permissionCreateRequestDTO);

        permission = permissionRepository.save(permission);

        return PermissionConverter.convertPermissionToPermissionResponseDTO(permission);
    }

    public Page<AllPermissionResponseDTO> findAll(Pageable pageable) {
        Page<Permission> permissionPage = permissionRepository.findAll(pageable);

        return permissionPage.map(PermissionConverter::convertPermissionToAllPermissionResponseDTO);
    }

    public PermissionResponseDTO findById(UUID id) {
        Permission permission = findPermissionByIdAndVerifyIfExists(id);

        return PermissionConverter.convertPermissionToPermissionResponseDTO(permission);
    }

    private Permission findPermissionByIdAndVerifyIfExists(UUID id) {
        Optional<Permission> permissionOptional = permissionRepository.findById(id);
        boolean permissionExists = permissionOptional.isPresent();

        if (!permissionExists) {
            throw new PermissionNotFoundException("Permission not found. Id used: " + id);
        }

        return permissionOptional.get();
    }

}
