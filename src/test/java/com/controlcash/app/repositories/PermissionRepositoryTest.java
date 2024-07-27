package com.controlcash.app.repositories;

import com.controlcash.app.models.Permission;
import jakarta.persistence.EntityManager;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class PermissionRepositoryTest {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private EntityManager entityManager;

    private Permission permission;

    @BeforeEach
    void setUp() {
        permission = new Permission();
        permission.setDescription("Admin");
    }

    @Test
    void testSave_GivenAPermissionWithAllAttributes_ShouldSaveAndReturnPermission() {
        Permission actualPermission = permissionRepository.save(permission);

        Assertions.assertNotNull(actualPermission);
        Assertions.assertNotNull(actualPermission.getId());
        Assertions.assertEquals(permission.getAuthority(), actualPermission.getAuthority());
    }

    @Test
    void testSave_WhenDescriptionIsNull_ShouldThrowsADataIntegrityViolationException() {
        permission.setDescription(null);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> permissionRepository.save(permission));
    }

    @Test
    void testSave_WhenDescriptionAlreadyExists_ShouldThrowsAConstraintViolationException() {
        permissionRepository.save(permission);
        permission = new Permission();
        permission.setDescription("Admin");

        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            permissionRepository.save(permission);
            entityManager.flush();
        });
    }

    @Test
    void testFindAll_ShouldReturnAPermissionList() {
        permissionRepository.save(permission);
        permission = new Permission();
        permission.setDescription("User");
        permissionRepository.save(permission);

        List<Permission> permissions = permissionRepository.findAll();

        Assertions.assertNotNull(permissions);
        Assertions.assertEquals(2, permissions.size());
    }

    @Test
    void testFindAll_GivenAPageable_ShouldReturnAPermissionPage() {
        permissionRepository.save(permission);
        permission = new Permission();
        permission.setDescription("User");
        permissionRepository.save(permission);
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "description"));

        Page<Permission> permissions = permissionRepository.findAll(pageable);

        Assertions.assertNotNull(permissions);
        Assertions.assertTrue(permissions.hasContent());
    }

    @Test
    void testFindById_GivenAValidId_ShouldReturnAOptionalWithPermission() {
        permission = permissionRepository.save(permission);
        UUID expectedId = permission.getId();

        Optional<Permission> optionalPermission = permissionRepository.findById(expectedId);

        Assertions.assertTrue(optionalPermission.isPresent());
        Permission actualPermission = optionalPermission.get();
        Assertions.assertEquals(expectedId, actualPermission.getId());
    }

    @Test
    void testFindById_GivenANotValidId_ShouldReturnAEmptyOptionalPermission() {
        UUID id = UUID.randomUUID();

        Optional<Permission> optionalPermission = permissionRepository.findById(id);

        Assertions.assertTrue(optionalPermission.isEmpty());
    }

    @Test
    void testUpdate_ShouldUpdatePermission() {
        String expectedNewDescription = "User";
        permission = permissionRepository.save(permission);
        permission.setDescription("User");

        permissionRepository.save(permission);
        Permission actualPermission = permissionRepository.findById(permission.getId()).get();

        Assertions.assertNotNull(actualPermission);
        Assertions.assertEquals(permission.getId(), actualPermission.getId());
        Assertions.assertEquals(expectedNewDescription, actualPermission.getAuthority());
    }
}
