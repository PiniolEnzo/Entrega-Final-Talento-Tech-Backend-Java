package com.techlab.service;

import com.techlab.dto.category.CategoryResponse;
import com.techlab.entity.Category;

import java.util.List;

public interface ICategoryService {

    CategoryResponse findById(Short id);

    List<CategoryResponse> getAll();

    List<Category> findAll();

    Category getById(Short id);

}
