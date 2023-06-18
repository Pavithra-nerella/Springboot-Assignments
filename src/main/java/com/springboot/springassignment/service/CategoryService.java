package com.springboot.springassignment.service;

import com.springboot.springassignment.entity.Category;

import java.util.List;

public interface CategoryService {
    public List<Category> getAllCategories();

    public Category getCategoryById(int theId);

    public void save(Category theCategory);

    public void deleteById(int theId);
}
