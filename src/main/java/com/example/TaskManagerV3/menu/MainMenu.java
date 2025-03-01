package com.example.TaskManagerV3.menu;

import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class MainMenu {
    private final LoginMenu loginMenu;
    private final TaskMenu taskMenu;
    private final CategoryMenu categoryMenu;
    private final ReminderMenu reminderMenu;
    private boolean running = true;

    public MainMenu(LoginMenu loginMenu, TaskMenu taskMenu, CategoryMenu categoryMenu, ReminderMenu reminderMenu) {
        this.loginMenu = loginMenu;
        this.taskMenu = taskMenu;
        this.categoryMenu = categoryMenu;
        this.reminderMenu = reminderMenu;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (running) {
            // If user is not logged in, handle login/registration
            if (!loginMenu.isLoggedIn()) {
                loginMenu.display(scanner);
            } else {
                displayMainOptions();
                int choice = loginMenu.getUserOption(scanner);
                switch (choice) {
                    case 1 -> taskMenu.display(scanner);
                    case 2 -> categoryMenu.display(scanner);
                    case 3 -> reminderMenu.display(scanner);
                    case 4 -> {
                        loginMenu.logout();
                        System.out.println("Logged out successfully.");
                    }
                    case 5 -> {
                        System.out.println("Exiting application.");
                        running = false;
                    }
                    default -> System.out.println("Invalid option. Try again.");
                }
            }
        }
        scanner.close();
    }

    private void displayMainOptions() {
        System.out.println("\n=== Main Menu ===");
        System.out.println("1. Manage Tasks");
        System.out.println("2. Manage Categories");
        System.out.println("3. Manage Reminders");
        System.out.println("4. Logout");
        System.out.println("5. Exit");
    }
}
