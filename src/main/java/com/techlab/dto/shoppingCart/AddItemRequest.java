package com.techlab.dto.shoppingCart;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Request to add an item to the shopping cart, containing the product ID and quantity.")
public class AddItemRequest {
    @NotNull(message = "ID must be provided.")
    @Min(value = 0, message = "ID must be greater than zero.")
    @Schema(description = "ID of the product to add to the cart", example = "101")
    private Long productId;
}
