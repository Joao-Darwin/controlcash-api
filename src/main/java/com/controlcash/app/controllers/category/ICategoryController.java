package com.controlcash.app.controllers.category;

import com.controlcash.app.dtos.category.request.CategoryRequestDTO;
import org.springframework.http.ResponseEntity;

public interface ICategoryController {

    ResponseEntity<?> create(CategoryRequestDTO categoryRequestDTO);
    ResponseEntity<?> findAll(int page, int size, String sort);
}
