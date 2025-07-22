package com.techlab.service;

import com.techlab.dto.product.ProductRequest;
import com.techlab.dto.product.ProductResponse;
import com.techlab.entity.Product;

import java.util.List;

public interface IProductService {

    List<ProductResponse> getAll();

    List<Product> findAll();

    ProductResponse getProduct(long id);

    Product get(Long id);

    List<ProductResponse> getProductByName(String name);

    ProductResponse createProduct(ProductRequest request);

    ProductResponse updateProduct(Long id, ProductRequest request);

    void remove(long id);

    List<ProductResponse> getProductByCategoryId(Short id);

}
