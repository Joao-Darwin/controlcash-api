package com.controlcash.app.repositories;

import com.controlcash.app.models.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    public void setUp() {
        category = new Category();
        category.setName("Cards");
    }

    @Test
    void testSave_GivenCategoryWithAllAttributes_ShouldSaveAndReturnACategory() {
        Category actualCategory = categoryRepository.save(category);

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(category.getName(), actualCategory.getName());
    }

    @Test
    void testSave_WhenCategoryNameIsNull_ShouldThrowsADataIntegrityViolationException() {
        category.setName(null);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(category));
    }

    @Test
    void testFindAll_ReturnACategoryList() {
        categoryRepository.save(category);
        category = new Category();
        category.setName("Internet");
        categoryRepository.save(category);

        List<Category> actualCategoryList = categoryRepository.findAll();

        Assertions.assertNotNull(actualCategoryList);
        Assertions.assertEquals(2, actualCategoryList.size());
    }

    @Test
    void testFindById_GivenAnIdValid_ShouldReturnACategory() {
        category = categoryRepository.save(category);
        UUID expectedId = category.getId();

        Optional<Category> optionalCategory = categoryRepository.findById(expectedId);

        Assertions.assertTrue(optionalCategory.isPresent());
        Category actualCategory = optionalCategory.get();
        Assertions.assertEquals(category.getName(), actualCategory.getName());
    }

    @Test
    void testFindById_GivenAnIdNotValid_ShouldReturnAOptionalEmpty() {
        UUID uuid = UUID.randomUUID();

        Optional<Category> optionalCategory = categoryRepository.findById(uuid);

        Assertions.assertTrue(optionalCategory.isEmpty());
    }

    @Test
    void testUpdateCategory_ShouldUpdateTheCategory() {
        category = categoryRepository.save(category);
        String expectedNewCategoryName = "Home";
        category.setName(expectedNewCategoryName);

        Category actualCategory = categoryRepository.save(category);

        Assertions.assertEquals(expectedNewCategoryName, actualCategory.getName());
    }

    @Test
    void testDeleteById_GivenAnId_ShouldRemoveCategory() {
        category = categoryRepository.save(category);
        UUID uuid = category.getId();

        categoryRepository.deleteById(uuid);
        Optional<Category> optionalCategory = categoryRepository.findById(uuid);

        Assertions.assertTrue(optionalCategory.isEmpty());
    }
}
