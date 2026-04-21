package com.techlab.dto.product;

import com.techlab.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Product request DTO")
public class ProductRequest {
    @NotNull
    @Size(min = 5, max = 80, message = "Name must be between 5 and 80 characters.")
    @Schema(description = "Product name", example = "Laptop", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotNull
    @Size(min = 10, max = 200, message = "Description must be between 10 and 200 characters.")
    @Schema(description = "Product description", example = "A high-performance laptop", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;

    @NotNull
    @PositiveOrZero
    @Schema(description = "Product price", example = "999.99", requiredMode = Schema.RequiredMode.REQUIRED)
    private float price;

    @NotNull
    @Positive
    @Schema(description = "Category ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Short categoryId;

    @NotNull
    @Min(value = 0, message = "Stock must be greater than or equal to 0.")
    @Schema(description = "Stock quantity", example = "100", requiredMode = Schema.RequiredMode.REQUIRED)
    private int stock;

    @NotNull
    @URL(message = "Invalid image URL.")
    @Schema(description = "Product image URL", example = "http://example.com/image.jpg", requiredMode = Schema.RequiredMode.REQUIRED)
    private String imageUrl;
}
