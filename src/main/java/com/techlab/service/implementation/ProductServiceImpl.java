package com.techlab.service.implementation;

import com.techlab.dto.product.ProductRequest;
import com.techlab.dto.product.ProductResponse;
import com.techlab.entity.Product;
import com.techlab.entity.User;
import com.techlab.exception.CategoryNotFoundException;
import com.techlab.exception.ProductNotFoundException;
import com.techlab.exception.UserNotFoundException;
import com.techlab.mapper.ProductMapper;
import com.techlab.mapper.UserMapper;
import com.techlab.repository.ICategoryRepository;
import com.techlab.repository.IProductRepository;
import com.techlab.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("productService")
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    public final IProductRepository productRepository;
    public final ICategoryRepository categoryRepository;

    @Override
    public List<ProductResponse> getAll() {
        List<Product> p = productRepository.findAllWithCategory();
        if (p.isEmpty()){
            throw new ProductNotFoundException();
        }
        return ProductMapper.toProductResponse(p);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAllWithCategory();
    }

    @Override
    public ProductResponse getProduct(long id) {
        return ProductMapper
                .toProductResponse(productRepository
                        .findById(id)
                        .orElseThrow(ProductNotFoundException::new));
    }

    @Override
    public Product get(Long id){
        return productRepository
                .findById(id)
                .orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public List<ProductResponse> getProductByName(String name) {
        List<Product> products = productRepository.findByName(name);
        if (products.isEmpty()) {
            throw new ProductNotFoundException("No products found with the given name.");
        }
        return ProductMapper.toProductResponse(products);
    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        Product product = ProductMapper.toProduct(request);
        product.setCategory(categoryRepository.findById(request.getCategoryId()).orElseThrow(CategoryNotFoundException::new));
        productRepository.save(product);
        return ProductMapper.toProductResponse(product);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        product = ProductMapper.toProduct(request);
        product.setCategory(categoryRepository.findById(request.getCategoryId()).orElseThrow(CategoryNotFoundException::new));

        productRepository.save(product);

        return ProductMapper.toProductResponse(product);
    }

    @Override
    public void remove(long id) {
        if (!productRepository.existsById(id)){
            throw new ProductNotFoundException();
        }
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductResponse> getProductByCategoryId(Short id) {
        if (!categoryRepository.existsById(id)){
            throw new CategoryNotFoundException();
        }
        return ProductMapper.toProductResponse(productRepository.findByCategoryId(id));
    }

}
