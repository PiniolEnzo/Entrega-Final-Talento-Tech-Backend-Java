package com.techlab.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;

class OrderLineTest {
    @Test
    @DisplayName("Set and get OrderLine properties")
    void setAndGetOrderLineProperties() {
        OrderLine line = new OrderLine();
        line.setQuantity(3);
        line.setSubtotalPrice(10f);
        Assertions.assertEquals(3, line.getQuantity());
        Assertions.assertEquals(10f, line.getSubtotalPrice());
    }

    @Test
    @DisplayName("Set and get all OrderLine properties")
    void setAndGetAllOrderLineProperties() {
        OrderLine line = new OrderLine();
        Order order = new Order();
        Product product = new Product();
        line.setId(7L);
        line.setOrder(order);
        line.setProduct(product);
        line.setUnitPrice(15.5f);
        line.setQuantity(4);
        line.setSubtotalPrice(62f);
        LocalDateTime now = LocalDateTime.now();
        line.setCreatedAt(now);
        line.setUpdatedAt(now);
        Assertions.assertEquals(7L, line.getId());
        Assertions.assertEquals(order, line.getOrder());
        Assertions.assertEquals(product, line.getProduct());
        Assertions.assertEquals(15.5f, line.getUnitPrice());
        Assertions.assertEquals(4, line.getQuantity());
        Assertions.assertEquals(62f, line.getSubtotalPrice());
        Assertions.assertEquals(now, line.getCreatedAt());
        Assertions.assertEquals(now, line.getUpdatedAt());
    }

    @Test
    @DisplayName("OrderLine constructor sets fields correctly")
    void orderLineConstructorSetsFieldsCorrectly() {
        Product product = new Product();
        product.setPrice(10f);
        Order order = new Order();
        OrderLine line = new OrderLine(order, product, 3);
        Assertions.assertEquals(order, line.getOrder());
        Assertions.assertEquals(product, line.getProduct());
        Assertions.assertEquals(3, line.getQuantity());
        Assertions.assertEquals(10f, line.getUnitPrice());
        Assertions.assertEquals(30f, line.getSubtotalPrice());
    }
}
