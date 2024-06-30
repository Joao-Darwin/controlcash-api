package com.controlcash.app.services;

import com.controlcash.app.dtos.category.request.CategoryRequestDTO;
import com.controlcash.app.dtos.category.response.CategoryResponseDTO;
import com.controlcash.app.models.Category;
import com.controlcash.app.repositories.CategoryRepository;
import com.controlcash.app.utils.converters.CategoryConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponseDTO create(CategoryRequestDTO categoryRequestDTO) {
        Category category = CategoryConverter.convertCategoryRequestDTOToCategory(categoryRequestDTO);

        category = categoryRepository.save(category);

        return CategoryConverter.convertCategoryToCategoryResponseDTO(category);
    }
}
