package com.techlab.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Schema(description = "Product response DTO")
public class ProductResponse {
    @Schema(description = "Product ID", example = "1")
    private Long id;
    @Schema(description = "Product name", example = "Laptop")
    private String name;
    @Schema(description = "Product description", example = "A high-performance laptop")
    private String description;
    @Schema(description = "Product price", example = "999.99")
    private Float price;
    @Schema(description = "Product image URL", example = "http://example.com/image.jpg")
    private String imageUrl;
    @Schema(description = "Product category ID", example = "2")
    private Short categoryId;
    @Schema(description = "Product category name", example = "Electronics")
    private String categoryName;
}
