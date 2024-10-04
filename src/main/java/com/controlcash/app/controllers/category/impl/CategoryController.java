package com.controlcash.app.controllers.category.impl;

import com.controlcash.app.controllers.category.ICategoryController;
import com.controlcash.app.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${path-api}/categories")
public class CategoryController implements ICategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
}
