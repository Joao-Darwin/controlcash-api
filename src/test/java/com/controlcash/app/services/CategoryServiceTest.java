package com.controlcash.app.services;

import com.controlcash.app.dtos.category.request.CategoryRequestDTO;
import com.controlcash.app.dtos.category.response.CategoryResponseDTO;
import com.controlcash.app.exceptions.CategoryNotFoundException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
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

    @Test
    void testFindAll_GivenAPageable_ShouldReturnACategoryResponseDTOPage() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));
        Page<Category> categoryPage = Page.empty();
        Mockito.when(categoryRepository.findAll(Mockito.any(Pageable.class))).thenReturn(categoryPage);

        Page<CategoryResponseDTO> actualCategoryResponseDTO = categoryService.findAll(pageable);

        Assertions.assertNotNull(actualCategoryResponseDTO);
        Assertions.assertEquals(0, actualCategoryResponseDTO.getSize());
    }

    @Test
    void testFindById_GivenAValidId_ShouldReturnCategoryResponseDTO() {
        UUID expectedId = UUID.randomUUID();
        Category category = new Category(expectedId, "Electronics", List.of(), List.of());
        Optional<Category> optionalCategory = Mockito.mock();
        Mockito.when(optionalCategory.isPresent()).thenReturn(true);
        Mockito.when(optionalCategory.get()).thenReturn(category);
        Mockito.when(categoryRepository.findById(Mockito.any(UUID.class))).thenReturn(optionalCategory);

        CategoryResponseDTO actualCategoryResponseDTO = categoryService.findById(expectedId);

        Assertions.assertNotNull(actualCategoryResponseDTO);
        Assertions.assertEquals(category.getId(), actualCategoryResponseDTO.id());
        Assertions.assertEquals(category.getName(), actualCategoryResponseDTO.name());
    }

    @Test
    void testFindById_GivenANotValidId_ShouldThrowsACategoryNotFoundException() {
        UUID id = UUID.randomUUID();
        String expectedExceptionMessage = "Category not found. Id used: " + id;
        Optional<Category> optionalCategory = Mockito.mock();
        Mockito.when(optionalCategory.isPresent()).thenReturn(false);
        Mockito.when(categoryRepository.findById(Mockito.any(UUID.class))).thenReturn(optionalCategory);

        CategoryNotFoundException actualCategoryNotFoundException = Assertions.assertThrows(CategoryNotFoundException.class, () -> categoryService.findById(id));

        Assertions.assertEquals(expectedExceptionMessage, actualCategoryNotFoundException.getMessage());
    }
}
