package com.techlab.mapper;

import com.techlab.dto.shoppingCart.CartResponse;
import com.techlab.entity.ShoppingCart;

import java.util.ArrayList;

public class ShoppingCartMapper {
    public static CartResponse toCartResponse(ShoppingCart cart) {
        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setItems(CartItemMapper.toCartItemResponse(new ArrayList<>(cart.getItems())));
        response.setTotal(cart.getTotalPrice());
        return response;
    }
}
