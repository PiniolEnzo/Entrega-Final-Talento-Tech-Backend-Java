package com.techlab.controller;

import com.techlab.dto.product.ProductRequest;
import com.techlab.dto.product.ProductResponse;
import com.techlab.entity.Category;
import com.techlab.service.ICategoryService;
import com.techlab.service.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final IProductService productService;

    private final ICategoryService categoryService;

    @GetMapping
    public List<ProductResponse> getAllProducts(
            @RequestParam(name = "categoryId", required = false) Short categoryId
    ) {

        if (categoryId != null) {
            return productService.getProductByCategoryId(categoryId);
        }

        return productService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {

        return ResponseEntity.ok().body(productService.getProduct(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request,
            UriComponentsBuilder uriBuilder) {
        Category category = categoryService.getById(request.getCategoryId());

        ProductResponse productResponse = productService.createProduct(request);

        var uri = uriBuilder.path("/products/{id}").buildAndExpand(productResponse.getId()).toUri();

        return ResponseEntity.created(uri).body(productResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        Category category = categoryService.getById(request.getCategoryId());

        return ResponseEntity.ok().body(productService.updateProduct(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {

        productService.remove(id);
        return ResponseEntity.noContent().build();
    }
}