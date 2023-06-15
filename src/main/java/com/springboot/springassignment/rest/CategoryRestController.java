package com.springboot.springassignment.rest;

import com.springboot.springassignment.entity.Category;
import com.springboot.springassignment.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryRestController {

    private CategoryService categoryService;

    @Autowired
    public CategoryRestController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getCategoryById(@PathVariable("categoryId") int categoryId) {
        try {
            Category category = categoryService.getCategoryById(categoryId);
            if (category == null) {
                String errorMessage = "Category not found with ID - " + categoryId;
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
            }
            return ResponseEntity.ok(category);
        } catch (RuntimeException e) {
            String errorMessage = "Failed to retrieve category with ID - " + categoryId;
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }
    @PostMapping
    public ResponseEntity<Object> addCategory(@RequestBody Category theCategory) {
        if (theCategory.getName() == null || theCategory.getName().isEmpty()) {
            String errorMessage = "Invalid category";
            return ResponseEntity.badRequest().body(errorMessage);
        }

        theCategory.setId(0);
        categoryService.save(theCategory);

        return ResponseEntity.ok(theCategory);
    }

@PutMapping("/{categoryId}")
public ResponseEntity<Object> updateCategory(@PathVariable("categoryId") int categoryId, @RequestBody Category updatedCategory) {
    try {
        Category existingCategory = categoryService.getCategoryById(categoryId);
        if (existingCategory == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found with ID: " + categoryId);
        }

        if (updatedCategory.getName() != null && !updatedCategory.getName().isEmpty()) {
            existingCategory.setName(updatedCategory.getName());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid category");
        }

        categoryService.save(existingCategory);

        return ResponseEntity.ok(existingCategory);
    } catch (RuntimeException e) {
        String errorMessage = "Failed to update category with ID - " + categoryId;
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }
}

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable int categoryId) {
        try {
            Category existingCategory = categoryService.getCategoryById(categoryId);

            if (existingCategory == null) {
                String errorMessage = "Category not found with ID - " + categoryId;
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
            }

            categoryService.deleteById(categoryId);

            return ResponseEntity.ok("Deleted Category with ID - " + categoryId);
        } catch (RuntimeException e) {
            String errorMessage = "Failed to delete category with ID - " + categoryId;
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }
}
