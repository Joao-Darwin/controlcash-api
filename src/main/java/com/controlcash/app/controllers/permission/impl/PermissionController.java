package com.controlcash.app.controllers.permission.impl;

import com.controlcash.app.controllers.permission.IPermissionController;
import com.controlcash.app.services.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${path-api}/permissions")
public class PermissionController implements IPermissionController {
    private final PermissionService permissionService;

    @Autowired
    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }
}
