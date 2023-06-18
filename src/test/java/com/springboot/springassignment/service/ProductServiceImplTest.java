package com.springboot.springassignment.service;
import com.springboot.springassignment.dao.ProductRepository;
import com.springboot.springassignment.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductServiceImpl(productRepository);
    }


    @Test
    void testGetAllProducts() {
        // Prepare
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "Product 1", 10.0, null));
        products.add(new Product(2, "Product 2", 20.0, null));

        when(productRepository.findAll()).thenReturn(products);

        // Execute
        List<Product> result = productService.getAllProducts();

        // Verify
        assertEquals(products.size(), result.size());
        assertEquals(products.get(0), result.get(0));
        assertEquals(products.get(1), result.get(1));
        verify(productRepository, times(1)).findAll();
    }


    @Test
    void testGetProductById_ExistingId() {
        // Arrange
        int productId = 1;
        Product product = new Product();
        product.setId(productId);
        product.setName("Product 1");
        product.setPrice(10.0);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        Product result = productService.getProductById(productId);

        // Assert
        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals("Product 1", result.getName());
        assertEquals(10.0, result.getPrice());
    }

    @Test
    void testGetProductById_NonExistingId() {
        // Arrange
        int productId = 1;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act
        Product result = productService.getProductById(productId);

        // Assert
        assertNull(result);
    }

    @Test
    void testSaveProduct() {
        // Arrange
        Product product = new Product();
        product.setName("Product 1");
        product.setPrice(10.0);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        productService.save(product);

        // Assert
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testDeleteProductById() {
        // Arrange
        int productId = 1;

        // Act
        productService.deleteById(productId);

        // Assert
        verify(productRepository, times(1)).deleteById(productId);
    }
}
