package com.controlcash.app.controllers.category.impl;

import com.controlcash.app.controllers.category.ICategoryController;
import com.controlcash.app.dtos.category.request.CategoryRequestDTO;
import com.controlcash.app.dtos.category.response.CategoryResponseDTO;
import com.controlcash.app.exceptions.CategoryNotFoundException;
import com.controlcash.app.exceptions.ResponseEntityException;
import com.controlcash.app.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("${path-api}/categories")
public class CategoryController implements ICategoryController {

    private final CategoryService categoryService;

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

    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "asc") String sort) {

        Sort.Direction sortDirection = sort.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "name"));

        Page<CategoryResponseDTO> categoryResponseDTOPage = categoryService.findAll(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(categoryResponseDTOPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        try {
            CategoryResponseDTO categoryResponseDTO =  categoryService.findById(id);

            return ResponseEntity.status(HttpStatus.OK).body(categoryResponseDTO);
        } catch (CategoryNotFoundException categoryNotFoundException) {
            ResponseEntityException responseEntityException = new ResponseEntityException(Instant.now(), categoryNotFoundException.getMessage(), "");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseEntityException);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody CategoryRequestDTO categoryRequestDTO, @PathVariable UUID id) {
        try {
            CategoryResponseDTO categoryResponseDTO = categoryService.update(categoryRequestDTO, id);

            return ResponseEntity.status(HttpStatus.OK).body(categoryResponseDTO);
        } catch (CategoryNotFoundException categoryNotFoundException) {
            ResponseEntityException responseEntityException = new ResponseEntityException(Instant.now(), categoryNotFoundException.getMessage(), "");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseEntityException);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        try {
            categoryService.delete(id);

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (CategoryNotFoundException categoryNotFoundException) {
            ResponseEntityException responseEntityException = new ResponseEntityException(Instant.now(), categoryNotFoundException.getMessage(), "");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseEntityException);
        }
    }
}
