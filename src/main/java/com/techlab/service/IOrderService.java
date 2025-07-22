package com.techlab.service;

import com.techlab.dto.order.OrderResponse;

import java.util.List;

public interface IOrderService {
    List<OrderResponse> getAllOrders();

    OrderResponse getOrder(Long orderId);

}
