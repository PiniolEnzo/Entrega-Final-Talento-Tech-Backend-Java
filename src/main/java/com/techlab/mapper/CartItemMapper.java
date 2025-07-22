package com.techlab.mapper;

import com.techlab.dto.shoppingCart.CartItemResponse;
import com.techlab.entity.CartItem;

import java.util.List;
import java.util.stream.Collectors;

public class CartItemMapper {
    public static CartItemResponse toCartItemResponse(CartItem item) {
        CartItemResponse response = new CartItemResponse();
        response.setProduct(ProductMapper.toProductDto(item.getProduct()));
        response.setQuantity(item.getQuantity());
        response.setSubtotal(item.getSubtotalPrice());
        return response;
    }

    public static List<CartItemResponse> toCartItemResponse(List<CartItem> items) {
        return items.stream()
                .map(CartItemMapper::toCartItemResponse)
                .collect(Collectors.toList());
    }
}
