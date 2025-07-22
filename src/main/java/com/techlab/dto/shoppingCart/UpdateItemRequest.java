package com.techlab.dto.shoppingCart;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateItemRequest {
    @NotNull(message = "Quantity must be provided.")
    @Min(value = 0, message = "Quantity must be greater than zero.")
    @Max(value = 1000000, message = "Quantity must be less than or equal to 1,000,000.")
    private Integer quantity;
}