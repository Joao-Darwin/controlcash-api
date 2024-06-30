package com.controlcash.app.utils.converters;

import com.controlcash.app.dtos.category.request.CategoryRequestDTO;
import com.controlcash.app.dtos.category.response.CategoryResponseDTO;
import com.controlcash.app.models.Category;
import com.controlcash.app.models.Goal;
import com.controlcash.app.models.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CategoryConverterTest {

    @Test
    public void testConvertCategoryRequestDTOToCategory_ShouldReturnCategory() {
        String expectedName = "Credit Card";
        List<Goal> expectedGoals = List.of(new Goal());
        List<Transaction> expectedTransactions = List.of(new Transaction());
        CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO(expectedName, expectedGoals, expectedTransactions);

        Category category = CategoryConverter.convertCategoryRequestDTOToCategory(categoryRequestDTO);

        Assertions.assertEquals(expectedName, category.getName());
        Assertions.assertEquals(expectedGoals, category.getGoals());
        Assertions.assertEquals(expectedTransactions, category.getTransactions());
    }

    @Test
    public void testConvertCategoryToCategoryRequestDTO_ShouldReturnCategoryResponseDTO() {
        UUID expectedId = UUID.randomUUID();
        String expectedName = "Credit Card";
        List<Goal> expectedGoals = List.of(new Goal());
        List<Transaction> expectedTransactions = List.of(new Transaction());
        Category category = new Category(expectedId, expectedName, expectedGoals, expectedTransactions);

        CategoryResponseDTO categoryResponseDTO = CategoryConverter.convertCategoryToCategoryResponseDTO(category);

        Assertions.assertEquals(expectedId, categoryResponseDTO.id());
        Assertions.assertEquals(expectedName, categoryResponseDTO.name());
    }
}
