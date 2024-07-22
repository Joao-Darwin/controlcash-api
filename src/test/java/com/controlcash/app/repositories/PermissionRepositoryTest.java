package com.controlcash.app.repositories;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class PermissionRepositoryTest {

    @Autowired
    private PermissionRepository permissionRepository;


}
