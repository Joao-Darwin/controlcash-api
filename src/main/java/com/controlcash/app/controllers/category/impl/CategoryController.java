package com.controlcash.app.controllers.category.impl;

import com.controlcash.app.controllers.category.ICategoryController;
import com.controlcash.app.dtos.category.request.CategoryRequestDTO;
import com.controlcash.app.dtos.category.response.CategoryResponseDTO;
import com.controlcash.app.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping
    public ResponseEntity<?> create(CategoryRequestDTO categoryRequestDTO) {
        try {
            CategoryResponseDTO categoryResponseDTO = categoryService.create(categoryRequestDTO);

            return ResponseEntity.status(HttpStatus.OK).body(categoryResponseDTO);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
