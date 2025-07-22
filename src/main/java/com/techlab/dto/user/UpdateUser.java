package com.techlab.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUser {
    @NotBlank(message = "Name is required.")
    @Size(min = 5, max = 25, message = "Description must be between 5 and 25 characters.")
    private String name;
}
