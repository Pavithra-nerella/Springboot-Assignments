package com.springboot.springassignment.rest;

import com.springboot.springassignment.entity.Product;
import com.springboot.springassignment.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ProductRestController productRestController = new ProductRestController(productService);
        mockMvc = MockMvcBuilders.standaloneSetup(productRestController).build();
    }

    @Test
    void getAllProducts() throws Exception {
        Product product1 = new Product(1, "Product 1", 10.0, null);
        Product product2 = new Product(2, "Product 2", 20.0, null);
        List<Product> products = Arrays.asList(product1, product2);

        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(products.size()))
                .andExpect(jsonPath("$[0].id").value(product1.getId()))
                .andExpect(jsonPath("$[0].name").value(product1.getName()))
                .andExpect(jsonPath("$[0].price").value(product1.getPrice()))
                .andExpect(jsonPath("$[1].id").value(product2.getId()))
                .andExpect(jsonPath("$[1].name").value(product2.getName()))
                .andExpect(jsonPath("$[1].price").value(product2.getPrice()));

        verify(productService, times(1)).getAllProducts();
        verifyNoMoreInteractions(productService);
    }

    @Test
    void getProductById() throws Exception {
        int productId = 1;
        Product product = new Product(productId, "Product 1", 10.0, null);

        when(productService.getProductById(productId)).thenReturn(product);

        mockMvc.perform(get("/products/{productId}", productId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.price").value(product.getPrice()));

        verify(productService, times(1)).getProductById(productId);
        verifyNoMoreInteractions(productService);
    }

    @Test
    void addProduct() throws Exception {
        Product product = new Product(1, "New Product", 15.0, null);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"New Product\",\"price\":15.0}"))
                .andExpect(status().isOk());

        verify(productService, times(1)).save(any(Product.class));
        verifyNoMoreInteractions(productService);
    }

@Test
void updateProduct() throws Exception {
    int productId = 1;
    Product product = new Product(productId, "Updated Product", 20.0, null);

    when(productService.getProductById(productId)).thenReturn(product);

    mockMvc.perform(put("/products/{productId}", productId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"id\":1,\"name\":\"Updated Product\",\"price\":20.0}"))
            .andExpect(status().isOk());

    verify(productService, times(1)).getProductById(productId);
    verify(productService, times(1)).save(any(Product.class));
    verifyNoMoreInteractions(productService);
}


    @Test
        void deleteProduct() throws Exception {
            int productId = 1;
            Product product = new Product(productId, "Product 1", 10.0, null);

            when(productService.getProductById(productId)).thenReturn(product);

            mockMvc.perform(delete("/products/{productId}", productId))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Deleted Product with ID - " + productId));

            verify(productService, times(1)).getProductById(productId);
            verify(productService, times(1)).deleteById(productId);
            verifyNoMoreInteractions(productService);
        }

        @Test
        void getAllProducts_ShouldReturnListOfProducts() throws Exception {
            // Arrange
            Product product1 = new Product(1, "Product 1", 10.0, null);
            Product product2 = new Product(2, "Product 2", 20.0, null);
            List<Product> products = Arrays.asList(product1, product2);
            when(productService.getAllProducts()).thenReturn(products);

            // Act and Assert
            mockMvc.perform(get("/products"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.length()").value(products.size()))
                    .andExpect(jsonPath("$[0].id").value(product1.getId()))
                    .andExpect(jsonPath("$[0].name").value(product1.getName()))
                    .andExpect(jsonPath("$[0].price").value(product1.getPrice()))
                    .andExpect(jsonPath("$[1].id").value(product2.getId()))
                    .andExpect(jsonPath("$[1].name").value(product2.getName()))
                    .andExpect(jsonPath("$[1].price").value(product2.getPrice()));

            // Verify
            verify(productService, times(1)).getAllProducts();
            verifyNoMoreInteractions(productService);
        }

        @Test
        void getProductById_ExistingProductId_ShouldReturnProduct() throws Exception {
            // Arrange
            int productId = 1;
            Product product = new Product(productId, "Product 1", 10.0, null);
            when(productService.getProductById(productId)).thenReturn(product);

            // Act and Assert
            mockMvc.perform(get("/products/{productId}", productId))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(product.getId()))
                    .andExpect(jsonPath("$.name").value(product.getName()))
                    .andExpect(jsonPath("$.price").value(product.getPrice()));

            // Verify
            verify(productService, times(1)).getProductById(productId);
            verifyNoMoreInteractions(productService);
        }

        @Test
        void getProductById_NonExistingProductId_ShouldThrowException() throws Exception {
            // Arrange
            int productId = 1;
            when(productService.getProductById(productId)).thenReturn(null);

            // Act and Assert
            mockMvc.perform(get("/products/{productId}", productId))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Product not found with ID - " + productId));

            // Verify
            verify(productService, times(1)).getProductById(productId);
            verifyNoMoreInteractions(productService);
        }

   @Test
   void addProduct_ValidProduct_ShouldReturnAddedProduct() throws Exception {
    // Arrange
    Product product = new Product(1, "New Product", 15.0, null);

    // Act and Assert
    mockMvc.perform(post("/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"New Product\",\"price\":15.0}")) // Remove the id from the request body
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(0)) // Update the expected value to 0
            .andExpect(jsonPath("$.name").value(product.getName()))
            .andExpect(jsonPath("$.price").value(product.getPrice()));

    // Verify
    verify(productService, times(1)).save(any(Product.class));
    verifyNoMoreInteractions(productService);
}

@Test
void updateProduct_ValidProduct_ShouldReturnUpdatedProduct() throws Exception {
    // Arrange
    int productId = 1;
    Product existingProduct = new Product(productId, "Existing Product", 10.0, null);
    Product updatedProduct = new Product(productId, "Updated Product", 20.0, null);

    when(productService.getProductById(productId)).thenReturn(existingProduct);

    // Act and Assert
    mockMvc.perform(put("/products/{productId}", productId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"id\":1,\"name\":\"Updated Product\",\"price\":20.0}"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(existingProduct.getId()))
            .andExpect(jsonPath("$.name").value(updatedProduct.getName()))
            .andExpect(jsonPath("$.price").value(updatedProduct.getPrice()));

    // Verify
    verify(productService, times(1)).getProductById(productId);
    verify(productService, times(1)).save(existingProduct);
    verifyNoMoreInteractions(productService);
}

    @Test
    void updateProduct_NonExistingProductId_ShouldThrowException() throws Exception {
        // Arrange
        int productId = 1;
        when(productService.getProductById(productId)).thenReturn(null);

        // Act and Assert
        mockMvc.perform(put("/products/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Updated Product\",\"price\":20.0}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Product not found with ID: " + productId));

        // Verify
        verify(productService, times(1)).getProductById(productId);
        verifyNoMoreInteractions(productService);
    }

    @Test
    void deleteProduct_NonExistingProductId_ShouldThrowException() throws Exception {
        // Arrange
        int productId = 1;
        when(productService.getProductById(productId)).thenReturn(null);

        // Act and Assert
        MvcResult result = mockMvc.perform(delete("/products/{productId}", productId))
                .andExpect(status().isNotFound())
                .andReturn();

        String errorMessage = "Product not found with ID - " + productId;
        String responseBody = result.getResponse().getContentAsString();

        assertEquals(errorMessage, responseBody);

        // Verify
        verify(productService, times(1)).getProductById(productId);
        verifyNoMoreInteractions(productService);
    }

    @Test
    void addProduct_InvalidProduct_ShouldReturnErrorResponse() throws Exception {
        // Arrange
        Product product = new Product(1, null, 15.0, null); // Invalid product with null name

        // Act and Assert
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":null,\"price\":15.0}")) // Update the request body with null name
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid product"));

        // Verify
        verifyNoInteractions(productService);
    }

@Test
void updateProduct_InvalidProduct_ShouldReturnErrorResponse() throws Exception {
    // Arrange
    int productId = 1;
    Product product = new Product(productId, null, 20.0, null); // Invalid product with null name
    when(productService.getProductById(productId)).thenReturn(product);

    // Act and Assert
    mockMvc.perform(put("/products/{productId}", productId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"id\":1,\"name\":null,\"price\":20.0}"))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Invalid product"));

    // Verify
    verify(productService, times(1)).getProductById(productId);
    verify(productService, times(1)).save(product);
    verifyNoMoreInteractions(productService);
}



    @Test
    void deleteProduct_ExistingProductId_ShouldReturnSuccessMessage() throws Exception {
        // Arrange
        int productId = 1;
        Product product = new Product(productId, "Product 1", 10.0, null);
        when(productService.getProductById(productId)).thenReturn(product);
        doNothing().when(productService).deleteById(productId);

        // Act and Assert
        mockMvc.perform(delete("/products/{productId}", productId))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted Product with ID - " + productId));

        // Verify
        verify(productService, times(1)).getProductById(productId);
        verify(productService, times(1)).deleteById(productId);
        verifyNoMoreInteractions(productService);
    }

    @Test
    void deleteProduct_NonExistingProductId_ShouldReturnErrorResponse() throws Exception {
        // Arrange
        int productId = 1;
        when(productService.getProductById(productId)).thenReturn(null);

        // Act and Assert
        mockMvc.perform(delete("/products/{productId}", productId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Product not found with ID - " + productId));

        // Verify
        verify(productService, times(1)).getProductById(productId);
        verifyNoMoreInteractions(productService);
    }

    @Test
    void updateProduct_nonExistentProduct_NotFound() throws Exception {
        // Arrange
        int productId = 1;
        Product updatedProduct = new Product(productId, "Updated Product", 20.0, null);

        when(productService.getProductById(productId)).thenReturn(null);

        // Act
        mockMvc.perform(put("/products/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Updated Product\",\"price\":20.0}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Product not found with ID: " + productId));

        // Assert
        verify(productService, times(1)).getProductById(productId);
        verifyNoMoreInteractions(productService);
    }


}





