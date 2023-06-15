package com.springboot.springassignment.dao;

import com.springboot.springassignment.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}