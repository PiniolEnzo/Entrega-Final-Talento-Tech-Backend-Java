package com.techlab.service.implementation;

import com.techlab.dto.category.CategoryResponse;
import com.techlab.entity.Category;
import com.techlab.exception.CategoryNotFoundException;
import com.techlab.exception.ProductNotFoundException;
import com.techlab.mapper.CategoryMapper;
import com.techlab.repository.ICategoryRepository;
import com.techlab.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service("categoryService")
public class CategoryServiceImpl implements ICategoryService {

    private final ICategoryRepository categoryRepository;

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
}
