package com.example.TaskManagerV3.service;

import com.example.TaskManagerV3.model.Category;
import com.example.TaskManagerV3.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // CREATE: Create a new category
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    // UPDATE: Update an existing category
    public Category updateCategory(Category category) {
        if (category.getCategoryId() == null || !categoryRepository.existsById(category.getCategoryId())) {
            throw new RuntimeException("Category not found with ID: " + category.getCategoryId());
        }
        return categoryRepository.save(category);
    }

    // DELETE: Delete a category by ID
    public void deleteCategory(long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        } else {
            throw new RuntimeException("Category not found with ID: " + id);
        }
    }

    // READ: Retrieve all categories
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
