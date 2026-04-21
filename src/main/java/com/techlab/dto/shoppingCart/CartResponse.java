package com.techlab.dto.shoppingCart;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Represents the response for a shopping cart.
 * Contains the cart ID, a list of items in the cart, and the total price.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response containing the shopping cart data")
public class CartResponse {
    @Schema(description = "Cart ID", example = "1")
    private Long id;
    @Schema(description = "List of items in the cart")
    private List<CartItemResponse> items;
    @Schema(description = "Total cart price", example = "149999.99")
    private Float total;
}
