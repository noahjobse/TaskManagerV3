package com.example.TaskManagerV3.menu;

import com.example.TaskManagerV3.model.User;
import com.example.TaskManagerV3.service.UserService;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class LoginMenu {
    private final UserService userService;
    private User loggedInUser;

    public LoginMenu(UserService userService) {
        this.userService = userService;
    }

    public void display(Scanner scanner) {
        System.out.println("\n=== Login Menu ===");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");

        int choice = getUserOption(scanner);
        switch (choice) {
            case 1 -> registerUser(scanner);
            case 2 -> login(scanner);
            case 3 -> {
                System.out.println("Exiting application.");
                System.exit(0);
            }
            default -> System.out.println("Invalid option.");
        }
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
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    private void registerUser(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        try {
            User newUser = new User(null, username, password, "User");
            userService.registerUser(newUser);
            System.out.println("Registration successful! Please log in.");
        } catch (RuntimeException e) {
            System.out.println("Registration error: " + e.getMessage());
        }
    }

    public boolean isLoggedIn() {
        return loggedInUser != null;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void logout() {
        loggedInUser = null;
    }

    public int getUserOption(Scanner scanner) {
        int option;
        while (true) {
            System.out.print("Enter your choice: ");
            try {
                option = Integer.parseInt(scanner.nextLine().trim());
                return option;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }
}
