package com.techlab.dto.shoppingCart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddItemRequest {
    @NotNull(message = "ID must be provided.")
    @Min(value = 0, message = "ID must be greater than zero.")
    private Long productId;
}
