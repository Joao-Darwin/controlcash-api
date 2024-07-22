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
}
