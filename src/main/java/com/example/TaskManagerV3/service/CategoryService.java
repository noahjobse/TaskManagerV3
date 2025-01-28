package com.example.TaskManagerV3.service;

import com.example.TaskManagerV3.model.Category;
import com.example.TaskManagerV3.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void createCategory(Category category) {
        categoryRepository.save(category);
    }
}