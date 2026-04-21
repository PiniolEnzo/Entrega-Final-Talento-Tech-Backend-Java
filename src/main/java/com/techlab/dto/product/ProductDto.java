package com.techlab.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Product response DTO")
public class ProductDto {
    @Schema(description = "Product ID", example = "1")
    private Long id;
    @Schema(description = "Product name", example = "Laptop")
    private String name;
    @Schema(description = "Product price", example = "999.99")
    private Float price;
}
