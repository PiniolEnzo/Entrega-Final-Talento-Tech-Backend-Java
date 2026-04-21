package com.techlab.dto.shoppingCart;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * UpdateItemRequest is a DTO class used to encapsulate the request data for updating an item in a shopping cart.
 * It includes validation constraints to ensure that the quantity is provided and falls within a specified range.
 */

@Data
@Schema(description = "Request to update the quantity of an item in the shopping cart")
public class UpdateItemRequest {
    @NotNull(message = "Quantity must be provided.")
    @Min(value = 0, message = "Quantity must be greater than zero.")
    @Max(value = 1000000, message = "Quantity must be less than or equal to 1,000,000.")
    @Schema(description = "Quantity of the item", example = "10", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0", maximum = "1000000")
    private Integer quantity;
}