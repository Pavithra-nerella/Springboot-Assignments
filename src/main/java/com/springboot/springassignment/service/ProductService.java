package com.springboot.springassignment.service;

import com.springboot.springassignment.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();

    Product getProductById(int theId);

    void save(Product theProduct);

    void deleteById(int theId);
}

