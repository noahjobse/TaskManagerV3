package com.example.TaskManagerV3.menu;

import com.example.TaskManagerV3.model.Category;
import com.example.TaskManagerV3.model.TaskEntity;
import com.example.TaskManagerV3.model.User;
import com.example.TaskManagerV3.service.CategoryService;
import com.example.TaskManagerV3.service.TaskService;
import com.example.TaskManagerV3.service.UserService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

@Component
public class Menu {

    private final TaskService taskService;
    private final UserService userService;
    private final CategoryService categoryService;
    private User loggedInUser;

    public Menu(TaskService taskService, UserService userService, CategoryService categoryService) {
        this.taskService = taskService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            if (loggedInUser == null) {
                displayLoginMenu();
                int option = getUserOption(scanner);
                running = handleLoginOption(option, scanner);
            } else {
                displayMainMenu();
                int option = getUserOption(scanner);
                running = handleMainOption(option, scanner);
            }
        }
        scanner.close();
    }

    private void displayLoginMenu() {
        System.out.println("\n=== Login Menu ===");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
    }

    private void displayMainMenu() {
        System.out.println("\n=== Main Menu ===");
        System.out.println("1. Create Task");
        System.out.println("2. View My Tasks");
        System.out.println("3. Manage Categories");
        System.out.println("4. Logout");
    }

    private boolean handleLoginOption(int option, Scanner scanner) {
        switch (option) {
            case 1 -> registerUser(scanner);
            case 2 -> login(scanner);
            case 3 -> {
                System.out.println("Exiting...");
                return false;
            }
            default -> System.out.println("Invalid option!");
        }
        return true;
    }

    private boolean handleMainOption(int option, Scanner scanner) {
        switch (option) {
            case 1 -> createTask(scanner);
            case 2 -> viewTasks();
            case 3 -> manageCategories(scanner);
            case 4 -> {
                loggedInUser = null;
                System.out.println("Logged out successfully.");
            }
            default -> System.out.println("Invalid option!");
        }
        return true;
    }

    private void login(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            loggedInUser = userService.authenticate(username, password);
            System.out.println("Welcome, " + loggedInUser.getUserName() + "!");
        } catch (RuntimeException e) {
            System.out.println("Invalid credentials: " + e.getMessage());
        }
    }

    private void createTask(Scanner scanner) {
        if (loggedInUser == null) {
            System.out.println("You must log in first!");
            return;
        }

        System.out.print("Enter task name: ");
        String taskName = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        List<Category> categories = categoryService.getAllCategories();
        if (categories.isEmpty()) {
            System.out.println("No categories available. Please create a category first.");
            return;
        }

        System.out.println("Select a category:");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i).getCategoryName());
        }
        int categoryIndex = getUserOption(scanner) - 1;
        if (categoryIndex < 0 || categoryIndex >= categories.size()) {
            System.out.println("Invalid category selection.");
            return;
        }
        Category selectedCategory = categories.get(categoryIndex);

        try {
            LocalDateTime dueDate = LocalDateTime.now().plusDays(7);
            TaskEntity task = new TaskEntity(null, taskName, description, "Pending", dueDate, loggedInUser, selectedCategory);
            taskService.createOrUpdateTask(task);
            System.out.println("Task created successfully!");
        } catch (RuntimeException e) {
            System.out.println("Error creating task: " + e.getMessage());
        }
    }

    private void viewTasks() {
        if (loggedInUser == null) {
            System.out.println("Please log in first.");
            return;
        }

        try {
            List<TaskEntity> tasks = taskService.getAllTasksByUser(loggedInUser.getUserId());
            if (tasks.isEmpty()) {
                System.out.println("No tasks found.");
            } else {
                System.out.println("\n=== Your Tasks ===");
                tasks.forEach(task -> System.out.printf(
                        "Task: %s | Description: %s | Status: %s | Category: %s%n",
                        task.getTaskName(),
                        task.getDescription(),
                        task.getStatus(),
                        (task.getCategory() != null) ? task.getCategory().getCategoryName() : "Uncategorized"
                ));
            }
        } catch (RuntimeException e) {
            System.out.println("Error retrieving tasks: " + e.getMessage());
        }
    }

    private void manageCategories(Scanner scanner) {
        System.out.println("\n=== Manage Categories ===");
        System.out.println("1. Create Category");
        System.out.println("2. View Categories");
        System.out.println("3. Back to Main Menu");

        int option = getUserOption(scanner);
        switch (option) {
            case 1 -> createCategory(scanner);
            case 2 -> viewCategories();
            case 3 -> {}
            default -> System.out.println("Invalid option!");
        }
    }

    private void createCategory(Scanner scanner) {
        System.out.print("Enter category name: ");
        String categoryName = scanner.nextLine();

        try {
            Category category = new Category(null, categoryName);
            categoryService.createOrUpdateCategory(category);
            System.out.println("Category created successfully!");
        } catch (RuntimeException e) {
            System.out.println("Error creating category: " + e.getMessage());
        }
    }

    private void viewCategories() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            if (categories.isEmpty()) {
                System.out.println("No categories found.");
            } else {
                System.out.println("\n=== Categories ===");
                categories.forEach(category -> System.out.println("Category: " + category.getCategoryName()));
            }
        } catch (RuntimeException e) {
            System.out.println("Error retrieving categories: " + e.getMessage());
        }
    }

    /**
     * Ensures the user enters a valid numeric choice.
     */
    private int getUserOption(Scanner scanner) {
        int option;
        while (true) {
            System.out.print("Enter your choice: ");
            try {
                option = Integer.parseInt(scanner.nextLine().trim());
                return option;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            }
        }
    }

    /**
     * Registers a new user by asking for username and password.
     */
    private void registerUser(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            User newUser = new User(null, username, password, "User");
            userService.registerUser(newUser);
            System.out.println("Registration successful! You can now log in.");
        } catch (RuntimeException e) {
            System.out.println("Error registering user: " + e.getMessage());
        }
    }
}
