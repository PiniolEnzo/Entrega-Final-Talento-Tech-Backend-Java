package com.techlab.service;

import com.techlab.dto.shoppingCart.CartItemResponse;
import com.techlab.dto.shoppingCart.CartResponse;
import com.techlab.entity.ShoppingCart;
import com.techlab.repository.IShoppingCartRepository;

public interface IShoppingCartService {

    CartResponse getCart(Long cartId);

    CartResponse createCart();

    CartItemResponse addToCart(Long cartId, Long productId);

    CartItemResponse updateItem(Long cartId, Long productId, Integer quantity);

    void removeItem(Long cartId, Long productId);

    void clearCart(Long cartId);



}
