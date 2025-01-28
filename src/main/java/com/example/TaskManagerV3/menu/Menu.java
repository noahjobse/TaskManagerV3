package com.example.TaskManagerV3.menu;

import com.example.TaskManagerV3.model.TaskEntity;
import com.example.TaskManagerV3.model.User;
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
    private User loggedInUser;

    // Constructor to inject TaskService and UserService
    public Menu(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    // Main method to start the menu
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

    // Display the login menu options
    private void displayLoginMenu() {
        System.out.println("\n=== Login Menu ===");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
    }

    // Display the main menu options
    private void displayMainMenu() {
        System.out.println("\n=== Main Menu ===");
        System.out.println("1. Create Task");
        System.out.println("2. Update Task");
        System.out.println("3. View My Tasks");
        System.out.println("4. Delete Task");
        System.out.println("5. Logout");
    }

    // Handle the login menu options
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

    // Handle the main menu options
    private boolean handleMainOption(int option, Scanner scanner) {
        switch (option) {
            case 1 -> createTask(scanner);
            case 2 -> updateTask(scanner);
            case 3 -> viewTasks();
            case 4 -> deleteTask(scanner);
            case 5 -> {
                loggedInUser = null;
                System.out.println("Logged out successfully.");
            }
            default -> System.out.println("Invalid option!");
        }
        return true;
    }

    // Login a user
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

    // Register a new user
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

    private void createTask(Scanner scanner) {
        if (loggedInUser == null) {
            System.out.println("You must log in first!");
            return;
        }

        System.out.print("Enter task name: ");
        String taskName = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter due date (yyyy-MM-dd HH:mm): ");
        String dueDateInput = scanner.nextLine();

        try {
            // Parse the due date string to LocalDateTime
            LocalDateTime dueDate = LocalDateTime.parse(dueDateInput.replace(" ", "T"));

            // Create a new TaskEntity object
            TaskEntity task = new TaskEntity(null, taskName, description, "Pending", dueDate, loggedInUser);

            // Save the task
            taskService.createOrUpdateTask(task);

            System.out.println("Task created successfully!");
        } catch (RuntimeException e) {
            System.out.println("Error creating task: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid due date format! Please use 'yyyy-MM-dd HH:mm'.");
        }
    }


    private void updateTask(Scanner scanner) {
        if (loggedInUser == null) {
            System.out.println("You must log in first!");
            return;
        }

        System.out.print("Enter task ID to update: ");
        Long taskId = scanner.nextLong();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter new task name: ");
        String taskName = scanner.nextLine();
        System.out.print("Enter new description: ");
        String description = scanner.nextLine();
        System.out.print("Enter new due date (yyyy-MM-dd HH:mm): ");
        String dueDateInput = scanner.nextLine();
        System.out.print("Enter new status (Pending/In Progress/Completed): ");
        String status = scanner.nextLine();

        try {
            // Retrieve the existing task by ID
            TaskEntity task = taskService.getTaskById(taskId);

            // Update task properties
            task.setTaskName(taskName);
            task.setDescription(description);

            // Parse and set the due date
            LocalDateTime dueDate = LocalDateTime.parse(dueDateInput.replace(" ", "T"));
            task.setDueDate(dueDate);

            task.setStatus(status);

            // Save the updated task
            taskService.createOrUpdateTask(task);

            System.out.println("Task updated successfully!");
        } catch (RuntimeException e) {
            System.out.println("Error updating task: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid due date format! Please use 'yyyy-MM-dd HH:mm'.");
        }
    }


    // View tasks of the logged-in user
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
                        "Task ID: %d | Task: %s | Description: %s | Due Date: %s | Status: %s%n",
                        task.getTaskId(),
                        task.getTaskName(),
                        task.getDescription(),
                        task.getDueDate(),
                        task.getStatus()
                ));
            }
        } catch (RuntimeException e) {
            System.out.println("Error retrieving tasks: " + e.getMessage());
        }
    }

    // Delete a task
    private void deleteTask(Scanner scanner) {
        if (loggedInUser == null) {
            System.out.println("You must log in first!");
            return;
        }

        System.out.print("Enter task ID to delete: ");
        Long taskId = scanner.nextLong();

        try {
            taskService.deleteTaskById(taskId);
            System.out.println("Task deleted successfully!");
        } catch (RuntimeException e) {
            System.out.println("Error deleting task: " + e.getMessage());
        }
    }

    // Get user option from the scanner
    private int getUserOption(Scanner scanner) {
        System.out.print("Enter your choice: ");
        int option = -1;
        try {
            option = scanner.nextInt();
            scanner.nextLine(); // Consume newline
        } catch (Exception e) {
            System.out.println("Invalid input! Please enter a number.");
            scanner.nextLine(); // Clear invalid input
        }
        return option;
    }
}
