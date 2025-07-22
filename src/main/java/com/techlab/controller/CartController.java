package com.techlab.controller;

import com.techlab.dto.shoppingCart.AddItemRequest;
import com.techlab.dto.shoppingCart.CartItemResponse;
import com.techlab.dto.shoppingCart.CartResponse;
import com.techlab.dto.shoppingCart.UpdateItemRequest;
import com.techlab.service.IShoppingCartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {
    private final IShoppingCartService cartService;

    @PostMapping
    public ResponseEntity<CartResponse> createCart(UriComponentsBuilder uriBuilder){
        CartResponse cartResponse = cartService.createCart();
        URI uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartResponse.getId()).toUri();

        return ResponseEntity.created(uri).body(cartResponse);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemResponse> addToCart(
            @PathVariable Long cartId,
            @Valid @RequestBody AddItemRequest request){

        CartItemResponse cartItemResponse = cartService.addToCart(cartId, request.getProductId());

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemResponse);
    }

    @GetMapping("/{cartId}")
    public CartResponse getCart(@PathVariable Long cartId){
        return cartService.getCart(cartId);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public CartItemResponse updateItem (
            @PathVariable("cartId") Long cartId,
            @PathVariable("productId") Long productId,
            @Valid@RequestBody UpdateItemRequest request){

        return cartService.updateItem(cartId, productId, request.getQuantity());
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<Void> removeItem(
            @PathVariable("cartId") Long cartId,
            @PathVariable("productId") Long productId) {

        cartService.removeItem(cartId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<Void> clearCart(@PathVariable Long cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }

}
