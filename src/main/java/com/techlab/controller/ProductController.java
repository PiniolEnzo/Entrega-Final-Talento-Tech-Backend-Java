package com.techlab.controller;

import com.techlab.dto.product.ProductRequest;
import com.techlab.dto.product.ProductResponse;
import com.techlab.entity.Category;
import com.techlab.service.ICategoryService;
import com.techlab.service.IProductService;
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
@RequestMapping("/products")
@Tag(name = "Products", description = "Operations for product catalog management")
public class ProductController {
    private final IProductService productService;

    private final ICategoryService categoryService;

    @Operation(
            summary = "Get all products",
            description = "Retrieve a list of all products. Optionally filter by categoryId"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class)))
    })
    @GetMapping
    public List<ProductResponse> getAllProducts(
            @RequestParam(name = "categoryId", required = false) Short categoryId
    ) {

        if (categoryId != null) {
            return productService.getProductByCategoryId(categoryId);
        }

        return productService.getAll();
    }

    @Operation(
            summary = "Get product by ID",
            description = "Retrieve a specific product by its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {

        return ResponseEntity.ok().body(productService.getProduct(id));
    }

    @Operation(
            summary = "Create product",
            description = "Create a new product in the catalog. Requires JWT authentication with ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request - invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request,
            UriComponentsBuilder uriBuilder) {
        Category category = categoryService.getById(request.getCategoryId());

        ProductResponse productResponse = productService.createProduct(request);

        var uri = uriBuilder.path("/products/{id}").buildAndExpand(productResponse.getId()).toUri();

        return ResponseEntity.created(uri).body(productResponse);
    }

    @Operation(
            summary = "Update product",
            description = "Update an existing product. Requires JWT authentication with ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request - invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role"),
            @ApiResponse(responseCode = "404", description = "Product or Category not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        Category category = categoryService.getById(request.getCategoryId());

        return ResponseEntity.ok().body(productService.updateProduct(id, request));
    }

    @Operation(
            summary = "Delete product",
            description = "Delete a product by its unique identifier. Requires JWT authentication with ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {

        productService.remove(id);
        return ResponseEntity.noContent().build();
    }
}