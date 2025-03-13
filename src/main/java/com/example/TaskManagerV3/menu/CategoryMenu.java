package com.example.TaskManagerV3.menu;

import com.example.TaskManagerV3.model.Category;
import com.example.TaskManagerV3.service.CategoryService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class CategoryMenu {
    private final CategoryService categoryService;
    private final LoginMenu loginMenu;

    public CategoryMenu(CategoryService categoryService, LoginMenu loginMenu) {
        this.categoryService = categoryService;
        this.loginMenu = loginMenu;
    }

    public void display(Scanner scanner) {
        boolean inCategoryMenu = true;
        while (inCategoryMenu) {
            System.out.println("\n=== Manage Categories ===");
            System.out.println("1. Create Category");
            System.out.println("2. View Categories");
            System.out.println("3. Delete Category");
            System.out.println("4. Back to Main Menu");
            int choice = loginMenu.getUserOption(scanner);
            switch (choice) {
                case 1 -> createCategory(scanner);
                case 2 -> viewCategories();
                case 3 -> deleteCategory(scanner);
                case 4 -> inCategoryMenu = false;
                default -> System.out.println("Invalid option!");
            }
        }
    }

    private void createCategory(Scanner scanner) {
        System.out.print("Enter category name: ");
        String categoryName = scanner.nextLine();
        Category category = new Category(null, categoryName);
        categoryService.createCategory(category);
        System.out.println("Category created successfully!");
    }

    private void viewCategories() {
        List<Category> categories = categoryService.getAllCategories();
        if (categories.isEmpty()) {
            System.out.println("No categories available.");
        } else {
            categories.forEach(c -> System.out.println("Category: " + c.getCategoryName()));
        }
    }

    private void deleteCategory(Scanner scanner) {
        System.out.print("Enter category ID to delete: ");
        int categoryId = loginMenu.getUserOption(scanner);
        categoryService.deleteCategory(categoryId);
        System.out.println("Category deleted successfully!");
    }
}
