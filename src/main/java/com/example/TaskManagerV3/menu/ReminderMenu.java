package com.example.TaskManagerV3.menu;

import com.example.TaskManagerV3.model.Reminder;
import com.example.TaskManagerV3.model.TaskEntity;
import com.example.TaskManagerV3.service.ReminderService;
import com.example.TaskManagerV3.service.TaskService;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

@Component
public class ReminderMenu {
    private final ReminderService reminderService;
    private final TaskService taskService;
    private final LoginMenu loginMenu;

    public ReminderMenu(ReminderService reminderService, TaskService taskService, LoginMenu loginMenu) {
        this.reminderService = reminderService;
        this.taskService = taskService;
        this.loginMenu = loginMenu;
    }

    public void display(Scanner scanner) {
        boolean inReminderMenu = true;
        while (inReminderMenu) {
            System.out.println("\n=== Manage Reminders ===");
            System.out.println("1. Create Reminder");
            System.out.println("2. View Reminders");
            System.out.println("3. Mark Reminder as Complete");
            System.out.println("4. Delete Reminder");
            System.out.println("5. Back to Main Menu");
            int choice = loginMenu.getUserOption(scanner);
            switch (choice) {
                case 1 -> createReminder(scanner);
                case 2 -> viewReminders();
                case 3 -> markReminderComplete(scanner);
                case 4 -> deleteReminder(scanner);
                case 5 -> inReminderMenu = false;
                default -> System.out.println("Invalid option!");
            }
        }
    }

    private void createReminder(Scanner scanner) {
        System.out.print("Enter Task ID for the reminder: ");
        int taskId = loginMenu.getUserOption(scanner);
        TaskEntity task = taskService.getTaskById((long) taskId);
        if (task == null) {
            System.out.println("Task not found!");
            return;
        }
        // Using current date/time for simplicity
        Date reminderDateTime = new Date();
        Reminder reminder = new Reminder(null, reminderDateTime, task);
        reminderService.createReminder(reminder);
        System.out.println("Reminder created successfully!");
    }

    private void viewReminders() {
        List<Reminder> reminders = reminderService.getRemindersByUser(loginMenu.getLoggedInUser().getUserId());
        if (reminders.isEmpty()) {
            System.out.println("No reminders found.");
        } else {
            reminders.forEach(r ->
                    System.out.printf("Reminder ID: %d | Task: %s | DateTime: %s | Completed: %b%n",
                            r.getReminderId(), r.getTask().getTaskName(), r.getReminderDateTime(), r.isComplete()));
        }
    }

    private void markReminderComplete(Scanner scanner) {
        System.out.print("Enter Reminder ID to mark as complete: ");
        int reminderId = loginMenu.getUserOption(scanner);
        reminderService.markReminderComplete((long) reminderId);
        System.out.println("Reminder marked as complete.");
    }

    private void deleteReminder(Scanner scanner) {
        System.out.print("Enter Reminder ID to delete: ");
        int reminderId = loginMenu.getUserOption(scanner);
        reminderService.deleteReminder((long) reminderId);
        System.out.println("Reminder deleted successfully!");
    }
}
