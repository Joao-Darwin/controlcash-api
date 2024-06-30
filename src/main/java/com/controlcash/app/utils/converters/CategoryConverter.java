package com.controlcash.app.utils.converters;

import com.controlcash.app.dtos.category.request.CategoryRequestDTO;
import com.controlcash.app.dtos.category.response.CategoryResponseDTO;
import com.controlcash.app.models.Category;

public class CategoryConverter {

    public Category convertCategoryRequestDTOToCategory(CategoryRequestDTO categoryRequestDTO) {
        Category category = new Category();

        category.setName(categoryRequestDTO.name());
        category.setGoals(categoryRequestDTO.goals());
        category.setTransactions(categoryRequestDTO.transactions());

        return category;
    }

    public CategoryResponseDTO convertCategoryToCategoryResponseDTO(Category category) {
        return new CategoryResponseDTO(category.getId(), category.getName());
    }
}
