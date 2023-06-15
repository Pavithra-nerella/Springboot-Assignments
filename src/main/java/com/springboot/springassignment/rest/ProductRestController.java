package com.springboot.springassignment.rest;

import com.springboot.springassignment.entity.Product;
import com.springboot.springassignment.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductRestController {
    private ProductService productService;

    @Autowired
    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

@GetMapping("/{productId}")
public ResponseEntity<?> getProductById(@PathVariable("productId") int productId) {
    Product product = productService.getProductById(productId);

    if (product == null) {
        String errorMessage = "Product not found with ID - " + productId;
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    return ResponseEntity.ok(product);
}

@PostMapping
public ResponseEntity<Object> addProduct(@RequestBody Product theProduct) {
    if (theProduct.getName() == null) {
        String errorMessage = "Invalid product";
        return ResponseEntity.badRequest().body(errorMessage);
    }
    theProduct.setId(0);
    productService.save(theProduct);

    return ResponseEntity.ok(theProduct);
}
@PutMapping("/{productId}")
public ResponseEntity<Object> updateProduct(@PathVariable("productId") int productId, @RequestBody Product updatedProduct) {
    Product existingProduct = productService.getProductById(productId);
    if (existingProduct == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found with ID: " + productId);
    }
    if (updatedProduct.getName() != null) {
        existingProduct.setName(updatedProduct.getName());
    }
    if (updatedProduct.getPrice() != 0) {
        existingProduct.setPrice(updatedProduct.getPrice());
    }
    if (updatedProduct.getCategory() != null) {
        existingProduct.setCategory(updatedProduct.getCategory());
    }
    productService.save(existingProduct);
    if (existingProduct.getName() == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid product");
    }
    return ResponseEntity.ok(existingProduct);
}

@DeleteMapping("/{productId}")
public ResponseEntity<String> deleteProduct(@PathVariable int productId) {
    Product tempProduct = productService.getProductById(productId);

    if (tempProduct == null) {
        String errorMessage = "Product not found with ID - " + productId;
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    productService.deleteById(productId);

    return ResponseEntity.ok("Deleted Product with ID - " + productId);
}


}

