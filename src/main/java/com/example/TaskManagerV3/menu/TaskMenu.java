package com.example.TaskManagerV3.menu;

import com.example.TaskManagerV3.model.Category;
import com.example.TaskManagerV3.model.TaskEntity;
import com.example.TaskManagerV3.model.User;
import com.example.TaskManagerV3.service.CategoryService;
import com.example.TaskManagerV3.service.TaskService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

@Component
public class TaskMenu {
    private final TaskService taskService;
    private final CategoryService categoryService;
    private final LoginMenu loginMenu;

    public TaskMenu(TaskService taskService, CategoryService categoryService, LoginMenu loginMenu) {
        this.taskService = taskService;
        this.categoryService = categoryService;
        this.loginMenu = loginMenu;
    }

    public void display(Scanner scanner) {
        boolean inTaskMenu = true;
        while (inTaskMenu) {
            System.out.println("\n=== Manage Tasks ===");
            System.out.println("1. Create Task");
            System.out.println("2. View My Tasks");
            System.out.println("3. Update Task");
            System.out.println("4. Delete Task");
            System.out.println("5. Back to Main Menu");
            int choice = loginMenu.getUserOption(scanner);
            switch (choice) {
                case 1 -> createTask(scanner);
                case 2 -> viewTasks();
                case 3 -> updateTask();
                case 4 -> deleteTask(scanner);
                case 5 -> inTaskMenu = false;
                default -> System.out.println("Invalid option!");
            }
        }
    }

    private void createTask(Scanner scanner) {
        User user = loginMenu.getLoggedInUser();
        if (user == null) {
            System.out.println("You must be logged in to create a task.");
            return;
        }
        System.out.print("Enter task name: ");
        String taskName = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        // For simplicity, we select the first available category (or handle selection here)
        List<Category> categories = categoryService.getAllCategories();
        if (categories.isEmpty()) {
            System.out.println("No categories available. Please create one first.");
            return;
        }
        Category selectedCategory = categories.getFirst();

        LocalDateTime dueDate = LocalDateTime.now().plusDays(7);
        TaskEntity task = new TaskEntity(null, taskName, description, "Pending", dueDate, user, selectedCategory);
        taskService.createTask(task);
        System.out.println("Task created successfully!");
    }

    private void viewTasks() {
        User user = loginMenu.getLoggedInUser();
        List<TaskEntity> tasks = taskService.getAllTasksByUser(user.getUserId());
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
        } else {
            tasks.forEach(task -> System.out.println("Task: " + task.getTaskName()));
        }
    }

    private void updateTask() {
        System.out.println("Update Task not implemented yet.");
    }

    private void deleteTask(Scanner scanner) {
        System.out.print("Enter Task ID to delete: ");
        int taskId = loginMenu.getUserOption(scanner);
        taskService.deleteTaskById((long) taskId);
        System.out.println("Task deleted successfully!");
    }
}
