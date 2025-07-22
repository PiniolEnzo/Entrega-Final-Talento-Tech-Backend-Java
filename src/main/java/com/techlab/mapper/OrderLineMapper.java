package com.techlab.mapper;

import com.techlab.dto.order.OrderLineResponse;
import com.techlab.entity.OrderLine;

import java.util.List;
import java.util.stream.Collectors;

public class OrderLineMapper {
    public static OrderLineResponse toOrderLineResponse(OrderLine line) {
        OrderLineResponse response = new OrderLineResponse();
        response.setProduct(ProductMapper.toProductDto(line.getProduct()));
        response.setQuantity(line.getQuantity());
        response.setSubtotalPrice(line.getSubtotalPrice());
        return response;
    }

    public static List<OrderLineResponse> toOrderLineResponse(List<OrderLine> lines) {
        return lines.stream()
                .map(OrderLineMapper::toOrderLineResponse)
                .collect(Collectors.toList());
    }
}
