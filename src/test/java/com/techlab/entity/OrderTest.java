package com.techlab.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

class OrderTest {
    @Test
    @DisplayName("Create Order with lines and total")
    void createOrderWithLinesAndTotal() {
        Order order = new Order();
        OrderLine line = new OrderLine();
        order.setLines(Set.of(line));
        order.setTotal(10f);
        Assertions.assertEquals(1, order.getLines().size());
        Assertions.assertEquals(10f, order.getTotal());
    }

    @Test
    @DisplayName("Order with empty lines")
    void orderWithEmptyLines() {
        Order order = new Order();
        order.setLines(Collections.emptySet());
        Assertions.assertTrue(order.getLines().isEmpty());
    }

    @Test
    @DisplayName("Set and get all Order properties")
    void setAndGetAllOrderProperties() {
        Order order = new Order();
        User user = new User();
        order.setId(100L);
        order.setCustomer(user);
        order.setPaymentStatus(PaymentStatus.PAID);
        order.setTotal(123.45f);
        LocalDateTime now = LocalDateTime.now();
        order.setCreatedAt(now);
        order.setUpdatedAt(now);
        Assertions.assertEquals(100L, order.getId());
        Assertions.assertEquals(user, order.getCustomer());
        Assertions.assertEquals(PaymentStatus.PAID, order.getPaymentStatus());
        Assertions.assertEquals(123.45f, order.getTotal());
        Assertions.assertEquals(now, order.getCreatedAt());
        Assertions.assertEquals(now, order.getUpdatedAt());
    }

    @Test
    @DisplayName("Order lines set is initialized and mutable")
    void orderLinesSetIsInitializedAndMutable() {
        Order order = new Order();
        OrderLine line = new OrderLine();
        order.getLines().add(line);
        Assertions.assertTrue(order.getLines().contains(line));
    }

    @Test
    @DisplayName("orderFromShoppingCart creates order correctly")
    void orderFromShoppingCartCreatesOrderCorrectly() {
        User user = new User();
        ShoppingCart cart = new ShoppingCart();
        CartItem item = new CartItem();
        Product product = new Product();
        product.setPrice(10f);
        item.setProduct(product);
        item.setQuantity(2);
        cart.setItems(Set.of(item));
        Order order = Order.orderFromShoppingCart(user, cart);
        Assertions.assertEquals(user, order.getCustomer());
        Assertions.assertEquals(PaymentStatus.PENDING, order.getPaymentStatus());
        Assertions.assertEquals(20f, order.getTotal());
        Assertions.assertFalse(order.getLines().isEmpty());
    }
}
