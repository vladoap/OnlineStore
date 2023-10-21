package com.example.MyStore.service.impl;

import com.example.MyStore.exception.CategoryNotFoundException;
import com.example.MyStore.model.entity.Category;
import com.example.MyStore.model.enums.CategoryNameEnum;
import com.example.MyStore.repository.CategoryRepository;
import com.example.MyStore.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category findByName(String category) {
        return categoryRepository
                .findByName(CategoryNameEnum.valueOf(category))
                .orElseThrow(() -> new CategoryNotFoundException("Category with name: " + category + " not found."));
    }
}
