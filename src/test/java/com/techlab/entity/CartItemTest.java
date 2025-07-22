package com.techlab.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class CartItemTest {
    @Test
    @DisplayName("Create CartItem with valid data")
    void createCartItemWithValidData() {
        Product product = new Product();
        CartItem item = new CartItem();
        item.setId(1L);
        item.setProduct(product);
        item.setQuantity(2);
        Assertions.assertEquals(1L, item.getId());
        Assertions.assertEquals(product, item.getProduct());
        Assertions.assertEquals(2, item.getQuantity());
    }

    @Test
    @DisplayName("Set and get quantity edge cases")
    void setAndGetQuantityEdgeCases() {
        CartItem item = new CartItem();
        item.setQuantity(0);
        Assertions.assertEquals(0, item.getQuantity());
        item.setQuantity(-1);
        Assertions.assertEquals(-1, item.getQuantity());
    }

    @Test
    @DisplayName("Set and get id, cart, product, quantity, createdAt, updatedAt")
    void setAndGetAllProperties() {
        CartItem item = new CartItem();
        ShoppingCart cart = new ShoppingCart();
        Product product = new Product();
        item.setId(10L);
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(5);
        LocalDateTime now = LocalDateTime.now();
        item.setCreatedAt(now);
        item.setUpdatedAt(now);
        Assertions.assertEquals(10L, item.getId());
        Assertions.assertEquals(cart, item.getCart());
        Assertions.assertEquals(product, item.getProduct());
        Assertions.assertEquals(5, item.getQuantity());
        Assertions.assertEquals(now, item.getCreatedAt());
        Assertions.assertEquals(now, item.getUpdatedAt());
    }

    @Test
    @DisplayName("getSubtotalPrice returns correct value")
    void getSubtotalPriceReturnsCorrectValue() {
        Product product = new Product();
        product.setPrice(20f);
        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(3);
        Assertions.assertEquals(60f, item.getSubtotalPrice());
    }

    @Test
    @DisplayName("getSubtotalPrice with null product return 0f")
    void getSubtotalPriceWithNullProduct() {
        CartItem item = new CartItem();
        item.setProduct(null);
        item.setQuantity(2);
        Assertions.assertEquals(0f, item.getSubtotalPrice());
    }
}
