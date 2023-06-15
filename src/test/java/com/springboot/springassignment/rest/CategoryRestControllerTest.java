package com.springboot.springassignment.rest;

import com.springboot.springassignment.entity.Category;
import com.springboot.springassignment.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CategoryRestControllerTest {

    @Mock
    private CategoryService categoryService;

    private CategoryRestController categoryRestController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        categoryRestController = new CategoryRestController(categoryService);
    }

    @Test
    void testGetAllCategories() {
        // Arrange
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1, "Category1"));
        categories.add(new Category(2, "Category2"));
        when(categoryService.getAllCategories()).thenReturn(categories);

        // Act
        List<Category> result = categoryRestController.getAllCategories();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Category1", result.get(0).getName());
        assertEquals("Category2", result.get(1).getName());
    }

    @Test
    void testGetCategoryById_ExistingCategory() {
        // Arrange
        int categoryId = 1;
        Category category = new Category(categoryId, "Category1");
        when(categoryService.getCategoryById(categoryId)).thenReturn(category);

        // Act
        ResponseEntity<?> response = categoryRestController.getCategoryById(categoryId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(category, response.getBody());
    }

    @Test
    void testGetCategoryById_NonExistingCategory() {
        // Arrange
        int categoryId = 1;
        when(categoryService.getCategoryById(categoryId)).thenReturn(null);

        // Act
        ResponseEntity<?> response = categoryRestController.getCategoryById(categoryId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Category not found with ID - " + categoryId, response.getBody());
    }

    @Test
    void testAddCategory_InvalidCategory() {
        // Arrange
        Category category = new Category(0, null);

        // Act
        ResponseEntity<Object> response = categoryRestController.addCategory(category);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid category", response.getBody());
        verify(categoryService, never()).save(any(Category.class));
    }


    @Test
    void testUpdateCategory_ExistingCategory_InvalidUpdate() {
        // Arrange
        int categoryId = 1;
        Category existingCategory = new Category(categoryId, "Category1");
        Category updatedCategory = new Category(categoryId, null);
        when(categoryService.getCategoryById(categoryId)).thenReturn(existingCategory);

        // Act
        ResponseEntity<Object> response = categoryRestController.updateCategory(categoryId, updatedCategory);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid category", response.getBody());
        verify(categoryService, never()).save(any(Category.class));
    }


    @Test
    void testUpdateCategory_NonExistingCategory() {
        // Arrange
        int categoryId = 1;
        Category updatedCategory = new Category(categoryId, "Updated Category");
        when(categoryService.getCategoryById(categoryId)).thenReturn(null);

        // Act
        ResponseEntity<Object> response = categoryRestController.updateCategory(categoryId, updatedCategory);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Category not found with ID: " + categoryId, response.getBody());
        verify(categoryService, never()).save(any(Category.class));
    }

    @Test
    void testDeleteCategory_ExistingCategory() {
        // Arrange
        int categoryId = 1;
        Category existingCategory = new Category(categoryId, "Category1");
        when(categoryService.getCategoryById(categoryId)).thenReturn(existingCategory);

        // Act
        ResponseEntity<String> response = categoryRestController.deleteCategory(categoryId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deleted Category with ID - " + categoryId, response.getBody());
        verify(categoryService, times(1)).deleteById(categoryId);
    }

    @Test
    void testDeleteCategory_NonExistingCategory() {
        // Arrange
        int categoryId = 1;
        when(categoryService.getCategoryById(categoryId)).thenReturn(null);

        // Act
        ResponseEntity<String> response = categoryRestController.deleteCategory(categoryId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Category not found with ID - " + categoryId, response.getBody());
        verify(categoryService, never()).deleteById(categoryId);
    }
    @Test
    void testGetAllCategories_ExceptionThrown() {
        // Arrange
        when(categoryService.getAllCategories()).thenThrow(new RuntimeException("Failed to retrieve categories"));

        // Act and Assert
        assertThrows(RuntimeException.class, () -> categoryRestController.getAllCategories());
    }

   }

