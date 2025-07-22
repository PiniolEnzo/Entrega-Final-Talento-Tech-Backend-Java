package com.techlab.mapper;

import com.techlab.dto.order.OrderResponse;
import com.techlab.entity.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {
    public static OrderResponse toOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setPaymentStatus(order.getPaymentStatus().name());
        response.setCreatedAt(order.getCreatedAt());
        response.setItems(OrderLineMapper.toOrderLineResponse(new ArrayList<>(order.getLines())));
        response.setTotalPrice(order.getTotal());
        return response;
    }

    public static List<OrderResponse> toOrderResponse(List<Order> orders) {
        return orders.stream()
                .map(OrderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }
}
