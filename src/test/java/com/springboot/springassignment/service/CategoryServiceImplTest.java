package com.springboot.springassignment.service;

import com.springboot.springassignment.dao.CategoryRepository;
import com.springboot.springassignment.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {

    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryService = new CategoryServiceImpl(categoryRepository);
    }

    @Test
    void testGetAllCategories() {
        // Prepare
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1, "Category 1"));
        categories.add(new Category(2, "Category 2"));

        when(categoryRepository.findAll()).thenReturn(categories);

        // Execute
        List<Category> result = categoryService.getAllCategories();

        // Verify
        assertEquals(categories.size(), result.size());
        assertEquals(categories.get(0), result.get(0));
        assertEquals(categories.get(1), result.get(1));
        verify(categoryRepository, times(1)).findAll();
    }


    @Test
    void testGetCategoryById() {
        // Prepare
        Category category = new Category(1, "Category 1");

        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

        // Execute
        Category result = categoryService.getCategoryById(1);

        // Verify
        assertEquals(category, result);
        verify(categoryRepository, times(1)).findById(1);
    }

    @Test
    void testGetCategoryById_ThrowsException() {
        // Prepare
        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        // Execute and Verify
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            categoryService.getCategoryById(1);
        });
        assertEquals("Did not find employee id - 1", exception.getMessage());
        verify(categoryRepository, times(1)).findById(1);
    }

    @Test
    void testSaveCategory() {
        // Prepare
        Category category = new Category(1, "Category 1");

        // Execute
        categoryService.save(category);

        // Verify
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void testDeleteCategoryById() {
        // Prepare
        int categoryId = 1;

        // Execute
        categoryService.deleteById(categoryId);

        // Verify
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }

}
