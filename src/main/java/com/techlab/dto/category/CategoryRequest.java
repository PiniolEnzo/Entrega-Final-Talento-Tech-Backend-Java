package com.techlab.dto.category;

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
@Schema(description = "Request object for creating or updating a product category.")
public class CategoryRequest {
    @NotNull
    @Size(min = 3, max = 25, message = "Name must be between 3 and 25 characters.")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name can only contain letters and spaces.")
    @Schema(description = "Category name", example = "Deco", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
}
