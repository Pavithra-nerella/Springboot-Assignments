package com.springboot.springassignment.service;

import com.springboot.springassignment.dao.CategoryRepository;
import com.springboot.springassignment.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

    @Service
    public class CategoryServiceImpl implements CategoryService {
        private CategoryRepository categoryRepository;

        @Autowired
        public CategoryServiceImpl(CategoryRepository thecategoryRepository) {
            categoryRepository = thecategoryRepository;
        }

        @Override
        public List<Category> getAllCategories() {
            return categoryRepository.findAll();
        }

        @Override
        public Category getCategoryById(int theId) {
            Optional<Category>result= categoryRepository.findById(theId);
            Category theCategory = null;

            if (result.isPresent()) {
                theCategory= result.get();
            }
            else {
                // we didn't find the employee
                throw new RuntimeException("Did not find employee id - " + theId);
            }

            return theCategory;
        }
        @Override
        public void save(Category theCategory) {
            categoryRepository.save(theCategory);
        }

        @Override
        public void deleteById(int theId) {
            categoryRepository.deleteById(theId);
        }

    }


