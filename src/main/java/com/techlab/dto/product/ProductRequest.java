package com.techlab.dto.product;

import com.techlab.entity.Category;
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
public class ProductRequest {
    @NotNull
    @Size(min = 5, max = 80, message = "Name must be between 5 and 80 characters.")
    private String name;

    @NotNull
    @Size(min = 10, max = 200, message = "Description must be between 10 and 200 characters.")
    private String description;

    @NotNull
    @PositiveOrZero
    private float price;

    @NotNull
    @Positive
    private Short categoryId;

    @NotNull
    @Min(value = 0, message = "Stock must be greater than or equal to 0.")
    private int stock;

    @NotNull
    @URL(message = "Invalid image URL.")
    private String imageUrl;
}
