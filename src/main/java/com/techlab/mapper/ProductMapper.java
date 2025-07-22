package com.techlab.mapper;

import com.techlab.dto.product.ProductRequest;
import com.techlab.dto.product.ProductResponse;
import com.techlab.dto.product.ProductDto;
import com.techlab.entity.Category;
import com.techlab.entity.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {

    // product response
    public static ProductResponse toProductResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setImageUrl(product.getImageUrl());
        response.setCategoryId(product.getCategory().getId());
        response.setCategoryName(product.getCategory().getName());

        return response;
    }

    public static List<ProductResponse> toProductResponse(List<Product> products){
        return products.stream()
                .map(ProductMapper::toProductResponse)
                .collect(Collectors.toList());
    }

    // product request
    public static Product toProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());

        product.setStock(request.getStock());
        product.setImageUrl(request.getImageUrl());
        return product;
    }

    // product dto para los carritos
    public static ProductDto toProductDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        return dto;
    }

    public static List<ProductDto> toProductDto(List<Product> products) {
        return products.stream()
                .map(ProductMapper::toProductDto)
                .collect(Collectors.toList());
    }

}
