package com.controlcash.app.controllers.category;

import com.controlcash.app.dtos.category.request.CategoryRequestDTO;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface ICategoryController {

    ResponseEntity<?> create(CategoryRequestDTO categoryRequestDTO);
    ResponseEntity<?> findAll(int page, int size, String sort);
    ResponseEntity<?> findById(UUID id);
    ResponseEntity<?> update(CategoryRequestDTO categoryRequestDTO, UUID id);
    ResponseEntity<?> delete(UUID id);
}
