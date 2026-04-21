package com.techlab.controller;

import com.techlab.dto.shoppingCart.AddItemRequest;
import com.techlab.dto.shoppingCart.CartItemResponse;
import com.techlab.dto.shoppingCart.CartResponse;
import com.techlab.dto.shoppingCart.UpdateItemRequest;
import com.techlab.entity.ShoppingCart;
import com.techlab.service.IShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
@Tag(name = "Shopping Cart", description = "Operations for shopping cart management")
public class CartController {
    private final IShoppingCartService cartService;


    @Operation(
            summary = "Create new cart",
            description = "Create a new empty shopping cart for the authenticated user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cart created successfully",
                    content = @Content(schema = @Schema(implementation = CartResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid")
    })
    @PostMapping
    public ResponseEntity<CartResponse> createCart(UriComponentsBuilder uriBuilder){
        CartResponse cartResponse = cartService.createCart();
        URI uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartResponse.getId()).toUri();

        return ResponseEntity.created(uri).body(cartResponse);
    }

    @Operation(
            summary = "Add item to cart",
            description = "Add a product to an existing shopping cart"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item added successfully",
                    content = @Content(schema = @Schema(implementation = CartItemResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request - invalid data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access denied - cart does not belong to user"),
            @ApiResponse(responseCode = "404", description = "Cart or Product not found")
    })
    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemResponse> addToCart(
            @PathVariable Long cartId,
            @Valid @RequestBody AddItemRequest request){

        CartItemResponse cartItemResponse = cartService.addToCart(cartId, request.getProductId());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemResponse);
    }

    @Operation(
            summary = "Get cart",
            description = "Retrieve a shopping cart with all its items"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart retrieved successfully",
                    content = @Content(schema = @Schema(implementation = CartResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access denied - cart does not belong to user"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @GetMapping("/{cartId}")
    public CartResponse getCart(@PathVariable Long cartId){
        return cartService.getCart(cartId);
    }

    @Operation(
            summary = "Update item quantity",
            description = "Update the quantity of a specific item in the cart"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item updated successfully",
                    content = @Content(schema = @Schema(implementation = CartItemResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request - invalid quantity"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Cart or Item not found")
    })
    @PutMapping("/{cartId}/items/{productId}")
    public CartItemResponse updateItem (
            @PathVariable("cartId") Long cartId,
            @PathVariable("productId") Long productId,
            @Valid@RequestBody UpdateItemRequest request){

        return cartService.updateItem(cartId, productId, request.getQuantity());
    }

    @Operation(
            summary = "Remove item",
            description = "Remove a specific item from the cart"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item removed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Cart or Item not found")
    })
    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<Void> removeItem(
            @PathVariable("cartId") Long cartId,
            @PathVariable("productId") Long productId) {

        cartService.removeItem(cartId, productId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Clear cart",
            description = "Remove all items from the cart (empty the cart)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cart cleared successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<Void> clearCart(@PathVariable Long cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }
}