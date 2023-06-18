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
    @Test
    void testAddCategory_ValidCategory() {
        // Arrange
        Category category = new Category(0, "New Category");

        // Act
        ResponseEntity<Object> response = categoryRestController.addCategory(category);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(category, response.getBody());
        verify(categoryService, times(1)).save(category);
    }

    @Test
    void testUpdateCategory_ExistingCategory_ValidUpdate() {
        // Arrange
        int categoryId = 1;
        Category existingCategory = new Category(categoryId, "Category1");
        Category updatedCategory = new Category(categoryId, "Updated Category");
        when(categoryService.getCategoryById(categoryId)).thenReturn(existingCategory);

        // Act
        ResponseEntity<Object> response = categoryRestController.updateCategory(categoryId, updatedCategory);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(existingCategory, response.getBody());
        assertEquals(updatedCategory.getName(), existingCategory.getName());
        verify(categoryService, times(1)).save(existingCategory);
    }
    @Test
    void getAllCategories() {
        // Create a list of categories for testing
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1, "Category 1"));
        categories.add(new Category(2, "Category 2"));

        // Mock the behavior of the categoryService.getAllCategories() method
        when(categoryService.getAllCategories()).thenReturn(categories);

        // Call the controller method
        List<Category> result = categoryRestController.getAllCategories();

        // Verify the result
        assertEquals(categories, result);
        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void getCategoryById_CategoryExists() {
        // Create a category for testing
        Category category = new Category(1, "Category 1");

        // Mock the behavior of the categoryService.getCategoryById() method
        when(categoryService.getCategoryById(1)).thenReturn(category);

        // Call the controller method
        ResponseEntity<?> responseEntity = categoryRestController.getCategoryById(1);

        // Verify the result
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(category, responseEntity.getBody());
        verify(categoryService, times(1)).getCategoryById(1);
    }

    @Test
    void getCategoryById_CategoryNotFound() {
        // Mock the behavior of the categoryService.getCategoryById() method
        when(categoryService.getCategoryById(1)).thenReturn(null);

        // Call the controller method
        ResponseEntity<?> responseEntity = categoryRestController.getCategoryById(1);

        // Verify the result
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Category not found with ID - 1", responseEntity.getBody());
        verify(categoryService, times(1)).getCategoryById(1);
    }

    @Test
    void getCategoryById_ExceptionThrown() {
        // Mock the behavior of the categoryService.getCategoryById() method to throw a RuntimeException
        when(categoryService.getCategoryById(1)).thenThrow(new RuntimeException("Failed to retrieve category"));

        // Call the controller method
        ResponseEntity<?> responseEntity = categoryRestController.getCategoryById(1);

        // Verify the result
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Failed to retrieve category with ID - 1", responseEntity.getBody());
        verify(categoryService, times(1)).getCategoryById(1);
    }

    @Test
    void addCategory_InvalidCategory() {
        // Create an invalid category for testing
        Category category = new Category();

        // Call the controller method
        ResponseEntity<Object> responseEntity = categoryRestController.addCategory(category);

        // Verify the result
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid category", responseEntity.getBody());
        verify(categoryService, never()).save(any(Category.class));
    }

    @Test
    void addCategory_ValidCategory() {
        // Create a valid category for testing
        Category category = new Category(1, "Category 1");

        // Call the controller method
        ResponseEntity<Object> responseEntity = categoryRestController.addCategory(category);

        // Verify the result
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(category, responseEntity.getBody());
        verify(categoryService, times(1)).save(category);
    }

    @Test
    void updateCategory_CategoryNotFound() {
        // Create an updated category for testing
        Category updatedCategory = new Category(1, "Updated Category");

        // Mock the behavior of the categoryService.getCategoryById() method
        when(categoryService.getCategoryById(1)).thenReturn(null);

        // Call the controller method
        ResponseEntity<Object> responseEntity = categoryRestController.updateCategory(1, updatedCategory);

        // Verify the result
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Category not found with ID: 1", responseEntity.getBody());
        verify(categoryService, times(1)).getCategoryById(1);
        verify(categoryService, never()).save(any(Category.class));
    }

    @Test
    void updateCategory_InvalidCategory() {
        // Create an existing category for testing
        Category existingCategory = new Category(1, "Category 1");

        // Mock the behavior of the categoryService.getCategoryById() method
        when(categoryService.getCategoryById(1)).thenReturn(existingCategory);

        // Create an invalid updated category for testing
        Category updatedCategory = new Category(1, "");

        // Call the controller method
        ResponseEntity<Object> responseEntity = categoryRestController.updateCategory(1, updatedCategory);

        // Verify the result
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid category", responseEntity.getBody());
        verify(categoryService, times(1)).getCategoryById(1);
        verify(categoryService, never()).save(any(Category.class));
    }

    @Test
    void updateCategory_ValidCategory() {
        // Create an existing category for testing
        Category existingCategory = new Category(1, "Category 1");

        // Mock the behavior of the categoryService.getCategoryById() method
        when(categoryService.getCategoryById(1)).thenReturn(existingCategory);

        // Create an updated category for testing
        Category updatedCategory = new Category(1, "Updated Category");

        // Call the controller method
        ResponseEntity<Object> responseEntity = categoryRestController.updateCategory(1, updatedCategory);

        // Verify the result
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedCategory, responseEntity.getBody());
        verify(categoryService, times(1)).getCategoryById(1);
        verify(categoryService, times(1)).save(updatedCategory);
    }

    @Test
    void deleteCategory_CategoryNotFound() {
        // Mock the behavior of the categoryService.getCategoryById() method
        when(categoryService.getCategoryById(1)).thenReturn(null);

        // Call the controller method
        ResponseEntity<String> responseEntity = categoryRestController.deleteCategory(1);

        // Verify the result
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Category not found with ID - 1", responseEntity.getBody());
        verify(categoryService, times(1)).getCategoryById(1);
        verify(categoryService, never()).deleteById(anyInt());
    }

    @Test
    void deleteCategory_ValidCategory() {
        // Create an existing category for testing
        Category existingCategory = new Category(1, "Category 1");

        // Mock the behavior of the categoryService.getCategoryById() method
        when(categoryService.getCategoryById(1)).thenReturn(existingCategory);

        // Call the controller method
        ResponseEntity<String> responseEntity = categoryRestController.deleteCategory(1);

        // Verify the result
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Deleted Category with ID - 1", responseEntity.getBody());
        verify(categoryService, times(1)).getCategoryById(1);
        verify(categoryService, times(1)).deleteById(1);
    }

    @Test
    void testGetCategoryById_ExceptionThrown() {
        // Arrange
        int categoryId = 1;
        when(categoryService.getCategoryById(categoryId)).thenThrow(new RuntimeException("Failed to retrieve category"));

        // Act
        ResponseEntity<?> response = categoryRestController.getCategoryById(categoryId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Failed to retrieve category with ID - " + categoryId, response.getBody());
        verify(categoryService, times(1)).getCategoryById(categoryId);
    }

    @Test
    void testDeleteCategory_ValidCategory() {
        // Arrange
        int categoryId = 1;
        Category existingCategory = new Category(categoryId, "Category1");
        when(categoryService.getCategoryById(categoryId)).thenReturn(existingCategory);

        // Act
        ResponseEntity<String> response = categoryRestController.deleteCategory(categoryId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deleted Category with ID - " + categoryId, response.getBody());
        verify(categoryService, times(1)).getCategoryById(categoryId);
        verify(categoryService, times(1)).deleteById(categoryId);
    }

    @Test
    void testDeleteCategory_ExceptionThrown() {
        // Arrange
        int categoryId = 1;
        when(categoryService.getCategoryById(categoryId)).thenThrow(new RuntimeException("Failed to retrieve category"));

        // Act
        ResponseEntity<String> response = categoryRestController.deleteCategory(categoryId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Failed to delete category with ID - " + categoryId, response.getBody());
        verify(categoryService, times(1)).getCategoryById(categoryId);
        verify(categoryService, never()).deleteById(categoryId);
    }

    @Test
    void testGetCategoryById_ValidCategory() {
        // Arrange
        int categoryId = 1;
        Category category = new Category(categoryId, "Category1");
        when(categoryService.getCategoryById(categoryId)).thenReturn(category);

        // Act
        ResponseEntity<?> response = categoryRestController.getCategoryById(categoryId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(category, response.getBody());
        verify(categoryService, times(1)).getCategoryById(categoryId);
    }
    @Test
    public void testUpdateCategoryException() {
        // Create a mock CategoryService
        CategoryService categoryService = mock(CategoryService.class);

        // Create a mock existing category
        Category existingCategory = mock(Category.class);

        // Create an updated category with an invalid name
        Category updatedCategory = new Category(1, null);

        // Mock the getCategoryById method to return the existing category
        when(categoryService.getCategoryById(1)).thenReturn(existingCategory);

        // Create an instance of CategoryRestController and inject the mock CategoryService
        CategoryRestController controller = new CategoryRestController(categoryService);

        // Call the updateCategory method
        ResponseEntity<Object> response = controller.updateCategory(1, updatedCategory);

        // Verify that the setName method of existingCategory is never called
        verify(existingCategory, never()).setName(anyString());

        // Verify that the categoryService save method is never called
        verify(categoryService, never()).save(existingCategory);

        // Verify that the response status is BAD_REQUEST
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        // Verify that the response body is the error message
        assertEquals("Invalid category", response.getBody());
    }
    @Test
    public void testUpdateCategory_Exception() {
        // Create a mock CategoryService
        CategoryService categoryService = mock(CategoryService.class);

        // Create an instance of CategoryRestController and inject the mock CategoryService
        CategoryRestController controller = new CategoryRestController(categoryService);

        // Specify the category ID
        int categoryId = 1;

        // Create a mock Category object for the existing category
        Category existingCategory = mock(Category.class);

        // Mock the categoryService getCategoryById method to return the existing category
        when(categoryService.getCategoryById(categoryId)).thenReturn(existingCategory);

        // Create an updated category with an invalid name (null)
        Category updatedCategory = new Category(categoryId, null);

        // Invoke the updateCategory method
        ResponseEntity<Object> response = controller.updateCategory(categoryId, updatedCategory);

        // Verify that the categoryService save method is never called
        verify(categoryService, never()).save(existingCategory);

        // Verify that the response status is BAD_REQUEST
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Verify that the response body is the expected error message
        assertEquals("Invalid category", response.getBody());
    }

}


