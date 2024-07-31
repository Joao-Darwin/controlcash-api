package com.controlcash.app.services;

import com.controlcash.app.dtos.category.request.CategoryRequestDTO;
import com.controlcash.app.dtos.category.response.CategoryResponseDTO;
import com.controlcash.app.models.Category;
import com.controlcash.app.repositories.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private CategoryRequestDTO categoryRequestDTO;

    @BeforeEach
    void setUp() {
        categoryRequestDTO = new CategoryRequestDTO("Electronics", List.of(), List.of());
    }

    @Test
    void testCreate_GivenACategoryRequestDTO_ShouldReturnACategoryResponseDTO() {
        Category expectedCategory = new Category(UUID.randomUUID(), "Electronics", List.of(), List.of());
        Mockito.when(categoryRepository.save(Mockito.any())).thenReturn(expectedCategory);

        CategoryResponseDTO actualCategory = categoryService.create(categoryRequestDTO);

        Assertions.assertNotNull(actualCategory);
        Assertions.assertEquals(expectedCategory.getId(), actualCategory.id());
        Assertions.assertEquals(expectedCategory.getName(), actualCategory.name());
    }
}
