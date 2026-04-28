package com.techlab.controller;

import com.techlab.dto.category.CategoryRequest;
import com.techlab.dto.category.CategoryResponse;
import com.techlab.entity.Category;
import com.techlab.service.ICategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
@Tag(name = "Categories", description = "Endpoints for managing product categories")
public class CategoryController {
    private final ICategoryService categoryService;

    @Operation(
            summary = "Get all categories",
            description = "Retrieve a list of all product categories"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "404", description = "No categories found")
    })
    @GetMapping()
    public ResponseEntity<List<CategoryResponse>> getCategories() {
        return ResponseEntity.ok().body(categoryService.getAll());
    }

    @Operation(
            summary = "Get all categories (raw)",
            description = "Retrieve a list of all product categories without mapping to DTO"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "404", description = "No categories found")
    })
    @GetMapping("/all")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok().body(categoryService.findAll());
    }

    @Operation(
            summary = "Get category by ID",
            description = "Retrieve a specific category by its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found",
                    content = @Content(schema = @Schema(implementation = CategoryResponse.class))),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable Short id) {
        return ResponseEntity.ok().body(categoryService.findById(id));
    }

    @Operation(
            summary = "Create a new category",
            description = "Add a new product category to the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully",
                    content = @Content(schema = @Schema(implementation = CategoryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid category data provided")
    })
    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody Category category, UriComponentsBuilder uriBuilder) {
        CategoryResponse createdCategory = categoryService.create(category);
        return ResponseEntity.created(uriBuilder.path("/categories/{id}").buildAndExpand(createdCategory.getId()).toUri()).body(createdCategory);
    }

    @Operation(
            summary = "Update an existing category",
            description = "Modify the details of an existing product category"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully",
                    content = @Content(schema = @Schema(implementation = CategoryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid category data provided"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateProduct(
            @PathVariable Short id,
            @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok().body(categoryService.update(id, request));
    }

    @Operation(
            summary = "Delete a category",
            description = "Remove a product category from the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Short id) {
        categoryService.remove(id);
        return ResponseEntity.noContent().build();
    }
}
