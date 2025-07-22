package com.techlab.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class CategoryTest {
    @Test
    @DisplayName("Create Category and set name")
    void createCategoryAndSetName() {
        Category category = new Category();
        category.setName("Electronics");
        Assertions.assertEquals("Electronics", category.getName());
    }

    @Test
    @DisplayName("Set and get all Category properties")
    void setAndGetAllCategoryProperties() {
        Category category = new Category();
        category.setId((short) 5);
        category.setName("Books");
        LocalDateTime now = LocalDateTime.now();
        category.setCreatedAt(now);
        category.setUpdatedAt(now);
        Assertions.assertEquals((short) 5, category.getId());
        Assertions.assertEquals("Books", category.getName());
        Assertions.assertEquals(now, category.getCreatedAt());
        Assertions.assertEquals(now, category.getUpdatedAt());
    }

    @Test
    @DisplayName("Category constructors work as expected")
    void categoryConstructorsWork() {
        Category c1 = new Category((short) 2);
        Assertions.assertEquals((short) 2, c1.getId());
        Category c2 = new Category("Toys");
        Assertions.assertEquals("Toys", c2.getName());
    }

    @Test
    @DisplayName("Products set is initialized and mutable")
    void productsSetIsInitializedAndMutable() {
        Category category = new Category();
        Assertions.assertNotNull(category.getProducts());
        Product product = new Product();
        category.getProducts().add(product);
        Assertions.assertTrue(category.getProducts().contains(product));
    }
}
