package com.techlab.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Set;

class ShoppingCartTest {
    @Test
    @DisplayName("Add and get CartItems")
    void addAndGetCartItems() {
        ShoppingCart cart = new ShoppingCart();
        CartItem item = new CartItem();
        cart.setItems(Set.of(item));
        Assertions.assertEquals(1, cart.getItems().size());
    }

    @Test
    @DisplayName("Set and get all ShoppingCart properties")
    void setAndGetAllShoppingCartProperties() {
        ShoppingCart cart = new ShoppingCart();
        cart.setId(42L);
        LocalDateTime now = LocalDateTime.now();
        cart.setCreatedAt(now);
        cart.setUpdatedAt(now);
        Assertions.assertEquals(42L, cart.getId());
        Assertions.assertEquals(now, cart.getCreatedAt());
        Assertions.assertEquals(now, cart.getUpdatedAt());
    }

    @Test
    @DisplayName("Items set is initialized and mutable")
    void itemsSetIsInitializedAndMutable() {
        ShoppingCart cart = new ShoppingCart();
        CartItem item = new CartItem();
        cart.getItems().add(item);
        Assertions.assertTrue(cart.getItems().contains(item));
    }

    @Test
    @DisplayName("getTotalPrice returns sum of all item subtotals")
    void getTotalPriceReturnsSumOfAllItemSubtotals() {
        ShoppingCart cart = new ShoppingCart();
        CartItem item1 = new CartItem();
        CartItem item2 = new CartItem();
        Product p1 = new Product();
        Product p2 = new Product();
        p1.setPrice(10f);
        p2.setPrice(5f);
        item1.setProduct(p1);
        item1.setQuantity(2); // subtotal: 20
        item2.setProduct(p2);
        item2.setQuantity(3); // subtotal: 15
        cart.getItems().add(item1);
        cart.getItems().add(item2);
        Assertions.assertEquals(35f, cart.getTotalPrice());
    }

    @Test
    @DisplayName("getTotalPrice with empty items returns zero")
    void getTotalPriceWithEmptyItemsReturnsZero() {
        ShoppingCart cart = new ShoppingCart();
        Assertions.assertEquals(0f, cart.getTotalPrice());
    }

    @Test
    @DisplayName("getItem returns correct CartItem by productId")
    void getItemReturnsCorrectCartItemByProductId() {
        ShoppingCart cart = new ShoppingCart();
        CartItem item = new CartItem();
        Product product = new Product();
        product.setId(99L);
        item.setProduct(product);
        cart.getItems().add(item);
        Assertions.assertEquals(item, cart.getItem(99L));
    }

    @Test
    @DisplayName("getItem returns null if productId not found")
    void getItemReturnsNullIfProductIdNotFound() {
        ShoppingCart cart = new ShoppingCart();
        Assertions.assertNull(cart.getItem(123L));
    }
}
