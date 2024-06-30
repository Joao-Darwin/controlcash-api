package com.controlcash.app.services;

import com.controlcash.app.dtos.category.request.CategoryRequestDTO;
import com.controlcash.app.dtos.category.response.CategoryResponseDTO;
import com.controlcash.app.exceptions.CategoryNotFoundException;
import com.controlcash.app.models.Category;
import com.controlcash.app.repositories.CategoryRepository;
import com.controlcash.app.utils.converters.CategoryConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

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

    public Page<CategoryResponseDTO> findAll(Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        return categoryPage.map(CategoryConverter::convertCategoryToCategoryResponseDTO);
    }

    public CategoryResponseDTO findById(UUID id) {
        Category category = findCategoryByIdAndVerifyIfExists(id);

        return CategoryConverter.convertCategoryToCategoryResponseDTO(category);
    }

    private Category findCategoryByIdAndVerifyIfExists(UUID id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        boolean categoryExists = categoryOptional.isPresent();

        if (!categoryExists) {
            throw new CategoryNotFoundException("Category not found. Id used: " + id);
        }

        return categoryOptional.get();
    }

    public CategoryResponseDTO update(CategoryRequestDTO categoryRequestDTO, UUID id) {
        Category category = findCategoryByIdAndVerifyIfExists(id);

        category.setName(categoryRequestDTO.name());

        return CategoryConverter.convertCategoryToCategoryResponseDTO(category);
    }

    public void delete(UUID id) {
        Category category = findCategoryByIdAndVerifyIfExists(id);

        categoryRepository.delete(category);
    }
}
