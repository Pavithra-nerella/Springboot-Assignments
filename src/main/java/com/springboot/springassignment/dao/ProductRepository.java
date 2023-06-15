package com.springboot.springassignment.dao;


import com.springboot.springassignment.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Integer> {
}

