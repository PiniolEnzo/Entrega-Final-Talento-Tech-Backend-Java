package com.techlab.dto.order;

import com.techlab.dto.product.ProductDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Order line item DTO")
public class OrderLineResponse {
    @Schema(description = "Product information")
    private ProductDto product;
    @Schema(description = "Quantity ordered", example = "2")
    private int quantity;
    @Schema(description = "Subtotal price for this line item", example = "299.98")
    private Float subtotalPrice;
}
