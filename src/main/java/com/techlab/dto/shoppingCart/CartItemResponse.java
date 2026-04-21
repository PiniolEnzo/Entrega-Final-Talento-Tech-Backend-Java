package com.techlab.dto.shoppingCart;

import com.techlab.dto.product.ProductDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO representing a single item in the shopping cart.
 * Includes the product, its quantity, and the subtotal cost.
 */

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Schema(description = "Response for a single item in the shopping cart")
public class CartItemResponse {
    @Schema(description = "Product associated with this cart item")
    private ProductDto product;
    @Schema(description = "Quantity of the product", example = "3")
    private int quantity;
    @Schema(description = "Subtotal price for the item", example = "749.97")
    private Float subtotal;

}
