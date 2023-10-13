package com.example.MyStore.service;

import com.example.MyStore.model.entity.Category;

public interface CategoryService {
    Category findByName(String category);
}
