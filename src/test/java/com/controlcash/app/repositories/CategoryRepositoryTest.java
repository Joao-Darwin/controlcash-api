package com.controlcash.app.repositories;

import com.controlcash.app.models.Category;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

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
}
