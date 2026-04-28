package com.techlab.service.implementation;

import com.techlab.dto.category.CategoryRequest;
import com.techlab.dto.category.CategoryResponse;
import com.techlab.entity.Category;
import com.techlab.exception.CategoryNotFoundException;
import com.techlab.exception.DuplicateCategory;
import com.techlab.mapper.CategoryMapper;
import com.techlab.repository.ICategoryRepository;
import com.techlab.repository.IProductRepository;
import com.techlab.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service("categoryService")
public class CategoryServiceImpl implements ICategoryService {

    private final ICategoryRepository categoryRepository;
    private final IProductRepository productRepository;

    @Override
    public CategoryResponse findById(Short id) {
        return CategoryMapper.
                toCategoryResponse(categoryRepository
                        .findById(id)
                        .orElseThrow(CategoryNotFoundException::new)
                );
    }

    @Override
    public List<CategoryResponse> getAll() {
        List<Category> c = categoryRepository.findAll();
        if (c.isEmpty()){
            throw new CategoryNotFoundException();
        }
        return CategoryMapper.toCategoryResponse(c);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getById(Short id){
        return categoryRepository
                .findById(id)
                .orElseThrow(CategoryNotFoundException::new);
    }

    @Override
    public CategoryResponse create(Category category) {
        if (categoryRepository.existsByName(category.getName())){
            throw new DuplicateCategory("Category with name " + category.getName() + " already exists");
        }
        return CategoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse update(Short id, CategoryRequest category) {
        Category existingCategory = categoryRepository
                .findById(id)
                .orElseThrow(CategoryNotFoundException::new);

        if (categoryRepository.existsByNameAndIdNot(category.getName(), id)){
            throw new DuplicateCategory("Category with name " + category.getName() + " already exists");
        }

        existingCategory.setName(category.getName());
        return CategoryMapper.toCategoryResponse(categoryRepository.save(existingCategory));
    }

    @Override
    public void remove(Short id) {
        Category category = categoryRepository
                .findById(id)
                .orElseThrow(CategoryNotFoundException::new);
        categoryRepository.delete(category);

        productRepository.findByCategoryId(id).forEach(product -> {
            product.setCategory(null);
            productRepository.save(product);
        });
    }

}
