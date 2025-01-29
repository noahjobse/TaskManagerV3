package com.example.TaskManagerV3.service;

import com.example.TaskManagerV3.model.Category;
import com.example.TaskManagerV3.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @SuppressWarnings("UnusedReturnValue")
    public Category createOrUpdateCategory(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteCategoryById(long id) { // Match long type with Category model
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        } else {
            throw new RuntimeException("Category not found with ID: " + id);
        }
    }

    private void deleteCategory(Scanner scanner) {
        System.out.print("Enter category ID to delete: ");
        long categoryId = scanner.nextLong(); // Match long type with Category model
        scanner.nextLine(); // Consume newline

        try {
            this.deleteCategoryById(categoryId);
            System.out.println("Category deleted successfully!");
        } catch (RuntimeException e) {
            System.out.println("Error deleting category: " + e.getMessage());
        }
    }
}
