package com.techlab.mapper;

import com.techlab.dto.category.CategoryRequest;
import com.techlab.dto.category.CategoryResponse;
import com.techlab.entity.Category;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryMapper {
    public static CategoryResponse toCategoryResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        return response;
    }

    public static List<CategoryResponse> toCategoryResponse(List<Category> categories) {
        return categories.stream()
                .map(CategoryMapper::toCategoryResponse)
                .collect(Collectors.toList());
    }


    public static Category toCategory(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        return category;
    }

    public static List<Category> toCategory(List<CategoryRequest> requests) {
        return requests.stream()
                .map(CategoryMapper::toCategory)
                .collect(Collectors.toList());
    }
}
