package com.springboot.springassignment.service;

import com.springboot.springassignment.dao.ProductRepository;
import com.springboot.springassignment.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

@Override
public Product getProductById(int theId) {
    Optional<Product> result = productRepository.findById(theId);

    if (result.isPresent()) {
        return result.get();
    } else {
        return null; // Return null instead of throwing an exception
    }
}


    @Override
    public void save(Product theProduct) {
        productRepository.save(theProduct);
    }

    @Override
    public void deleteById(int theId) {
        productRepository.deleteById(theId);
    }
}

