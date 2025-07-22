package com.techlab.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;

class ProductTest {
    @Test
    @DisplayName("Set and get Product name and price")
    void setAndGetProductNameAndPrice() {
        Product product = new Product();
        product.setName("Laptop");
        product.setPrice(999.99f);
        Assertions.assertEquals("Laptop", product.getName());
        Assertions.assertEquals(999.99f, product.getPrice());
    }

    @Test
    @DisplayName("Set and get all Product properties")
    void setAndGetAllProductProperties() {
        Product product = new Product();
        product.setId(5L);
        product.setName("Tablet");
        product.setDescription("Android tablet");
        product.setPrice(350.0f);
        Category category = new Category();
        product.setCategory(category);
        LocalDateTime now = LocalDateTime.now();
        product.setCreatedAt(now);
        product.setUpdatedAt(now);
        Assertions.assertEquals(5L, product.getId());
        Assertions.assertEquals("Tablet", product.getName());
        Assertions.assertEquals("Android tablet", product.getDescription());
        Assertions.assertEquals(350.0f, product.getPrice());
        Assertions.assertEquals(category, product.getCategory());
        Assertions.assertEquals(now, product.getCreatedAt());
        Assertions.assertEquals(now, product.getUpdatedAt());
    }

    @Test
    @DisplayName("Product can have null description and category")
    void productCanHaveNullDescriptionAndCategory() {
        Product product = new Product();
        product.setDescription(null);
        product.setCategory(null);
        Assertions.assertNull(product.getDescription());
        Assertions.assertNull(product.getCategory());
    }
}
