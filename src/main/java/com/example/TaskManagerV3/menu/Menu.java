package com.example.TaskManagerV3.menu;

import com.example.TaskManagerV3.model.Category;
import com.example.TaskManagerV3.model.Reminder;
import com.example.TaskManagerV3.model.TaskEntity;
import com.example.TaskManagerV3.model.User;
import com.example.TaskManagerV3.service.CategoryService;
import com.example.TaskManagerV3.service.ReminderService;
import com.example.TaskManagerV3.service.TaskService;
import com.example.TaskManagerV3.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

@Component
public class Menu {

    private final TaskService taskService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ReminderService reminderService;
    private final PlatformTransactionManager transactionManager;
    private User loggedInUser;

    public Menu(TaskService taskService,
                UserService userService,
                CategoryService categoryService,
                ReminderService reminderService,
                PlatformTransactionManager transactionManager) {
        this.taskService = taskService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.reminderService = reminderService;
        this.transactionManager = transactionManager;
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
        System.out.println("1. Manage Tasks");
        System.out.println("2. Manage Categories");
        System.out.println("3. Manage Reminders");
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
            case 1 -> manageTasks(scanner);
            case 2 -> manageCategories(scanner);
            case 3 -> manageReminders(scanner);
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

    /**
     * Manages tasks via a submenu that allows creating, viewing, and deleting tasks.
     */
    private void manageTasks(Scanner scanner) {
        boolean inTaskMenu = true;
        while (inTaskMenu) {
            System.out.println("\n=== Manage Tasks ===");
            System.out.println("1. Create Task");
            System.out.println("2. View My Tasks");
            System.out.println("3. Update Task");
            System.out.println("4. Delete Task");
            System.out.println("5. Back to Main Menu");
            int choice = getUserOption(scanner);
            switch (choice) {
                case 1 -> createTask(scanner);
                case 2 -> viewTasks(scanner);
                case 3 -> updateTask(scanner);
                case 4 -> deleteTask(scanner);
                case 5 -> inTaskMenu = false;
                default -> System.out.println("Invalid option!");
            }
        }
    }

    /**
     * Creates a new task and then optionally prompts the user to add a reminder to the task.
     */
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
            taskService.createTask(task);
            System.out.println("Task created successfully!");

            // Prompt the user to add a reminder to this task
            System.out.print("Would you like to add a reminder to this task? (yes/no): ");
            String response = scanner.nextLine().trim();
            if (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("y")) {
                Date reminderDateTime = inputReminderDateTime(scanner);
                if (reminderDateTime == null) {
                    System.out.println("Reminder not added due to invalid input.");
                } else {
                    Reminder reminder = new Reminder(null, reminderDateTime, task);
                    reminderService.createReminder(reminder);
                    System.out.println("Reminder added to task successfully!");
                }
            }
        } catch (RuntimeException e) {
            System.out.println("Error creating task: " + e.getMessage());
        }
    }

    private void updateTask(Scanner scanner) {
        List<TaskEntity> tasks = taskService.getAllTasksByUser(loggedInUser.getUserId());
        if (tasks.isEmpty()) {
            System.out.println("No tasks available to update.");
            return;
        }

        System.out.println("Select a task to update:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i).getTaskName());
        }
        int taskIndex = getUserOption(scanner) - 1;
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            System.out.println("Invalid task selection.");
            return;
        }

        TaskEntity selectedTask = taskService.getTaskWithReminders(tasks.get(taskIndex).getTaskId());

        System.out.print("Enter new task name (leave blank to keep current): ");
        String newTaskName = scanner.nextLine().trim();
        if (!newTaskName.isEmpty()) {
            selectedTask.setTaskName(newTaskName);
        }

        System.out.print("Enter new description (leave blank to keep current): ");
        String newDescription = scanner.nextLine().trim();
        if (!newDescription.isEmpty()) {
            selectedTask.setDescription(newDescription);
        }

        System.out.print("Enter new status (leave blank to keep current): ");
        String newStatus = scanner.nextLine().trim();
        if (!newStatus.isEmpty()) {
            selectedTask.setStatus(newStatus);
        }

        List<Category> categories = categoryService.getAllCategories();
        if (!categories.isEmpty()) {
            System.out.println("Select a new category (leave blank to keep current):");
            for (int i = 0; i < categories.size(); i++) {
                System.out.println((i + 1) + ". " + categories.get(i).getCategoryName());
            }
            String categoryInput = scanner.nextLine().trim();
            if (!categoryInput.isEmpty()) {
                int categoryIndex = Integer.parseInt(categoryInput) - 1;
                if (categoryIndex >= 0 && categoryIndex < categories.size()) {
                    selectedTask.setCategory(categories.get(categoryIndex));
                }
            }
        }

        // Prompt the user to add or update a reminder to this task
        System.out.print("Would you like to add or update a reminder for this task? (yes/no): ");
        String response = scanner.nextLine().trim();
        if (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("y")) {
            System.out.println("Please enter the reminder date and time in the following format:");
            Date reminderDateTime = inputReminderDateTime(scanner);
            if (reminderDateTime == null) {
                System.out.println("Reminder not added due to invalid input.");
            } else {
                Reminder reminder = new Reminder(null, reminderDateTime, selectedTask);
                reminderService.createReminder(reminder);
                System.out.println("Reminder added/updated successfully!");
            }
        }

        taskService.updateTask(selectedTask);
        System.out.println("Task updated successfully.");
    }


    /**
     * Views tasks along with associated reminder information.
     * This method wraps the task retrieval in a transaction so that lazy-loaded
     * collections (such as reminders) can be initialized.
     */
    private void viewTasks(Scanner scanner) {
        // Wrap the retrieval in a transaction so lazy-loaded collections are initialized
        TransactionTemplate txTemplate = new TransactionTemplate(transactionManager);
        txTemplate.execute(status -> {
            try {
                List<TaskEntity> tasks = taskService.getAllTasksByUser(loggedInUser.getUserId());
                if (tasks.isEmpty()) {
                    System.out.println("No tasks found.");
                } else {
                    System.out.println("\n=== Your Tasks ===");
                    for (TaskEntity task : tasks) {
                        System.out.println("--------------------------------------------------");
                        System.out.println("Task: " + task.getTaskName());
                        System.out.println("  Description: " + task.getDescription());
                        System.out.println("  Status: " + task.getStatus());
                        System.out.println("  Category: " + ((task.getCategory() != null)
                                ? task.getCategory().getCategoryName()
                                : "Uncategorized"));

                        List<Reminder> reminders = task.getReminders();
                        if (reminders != null && !reminders.isEmpty()) {
                            System.out.println("  Reminders:");
                            for (Reminder reminder : reminders) {
                                System.out.printf("    [ID: %d] %s (Completed: %b)%n",
                                        reminder.getReminderId(),
                                        reminder.getReminderDateTime(),
                                        reminder.isComplete());
                            }
                        } else {
                            System.out.println("  No reminders for this task.");
                        }
                        System.out.println("--------------------------------------------------\n");
                    }
                }
            } catch (RuntimeException e) {
                System.out.println("Error retrieving tasks: " + e.getMessage());
            }
            return null;
        });
    }


    /**
     * Deletes a task in deletion mode.
     * The method repeatedly shows the updated list of tasks until the user enters 0 to exit.
     */
    private void deleteTask(Scanner scanner) {
        while (true) {
            List<TaskEntity> tasks = taskService.getAllTasksByUser(loggedInUser.getUserId());
            if (tasks.isEmpty()) {
                System.out.println("No tasks available to delete.");
                break;
            }
            System.out.println("\nSelect a task to delete (enter 0 to exit deletion mode):");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + ". " + tasks.get(i).getTaskName());
            }
            int choice = getUserOption(scanner);
            if (choice == 0) {
                System.out.println("Exiting deletion mode.");
                break;
            }
            int index = choice - 1;
            if (index < 0 || index >= tasks.size()) {
                System.out.println("Invalid selection.");
                continue;
            }
            TaskEntity taskToDelete = tasks.get(index);
            System.out.print("Are you sure you want to delete the task '" + taskToDelete.getTaskName() + "'? (yes/no): ");
            String confirm = scanner.nextLine().trim();
            if (confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y")) {
                taskService.deleteTaskById(taskToDelete.getTaskId());
                System.out.println("Task deleted successfully.");
            } else {
                System.out.println("Task deletion cancelled.");
            }
            // Loop to refresh the task list and allow further deletions.
        }
    }

    private void manageCategories(Scanner scanner) {
        boolean inCategoryMenu = true;
        while (inCategoryMenu) {
            System.out.println("\n=== Manage Categories ===");
            System.out.println("1. Create Category");
            System.out.println("2. View Categories");
            System.out.println("3. Update Category");
            System.out.println("4. Delete Category");
            System.out.println("5. Back to Main Menu");

            int option = getUserOption(scanner);
            switch (option) {
                case 1 -> createCategory(scanner);
                case 2 -> viewCategories();
                case 3 -> updateCategory(scanner);
                case 4 -> deleteCategory(scanner);
                case 5 -> inCategoryMenu = false;
                default -> System.out.println("Invalid option!");
            }
        }
    }

    private void createCategory(Scanner scanner) {
        System.out.print("Enter category name: ");
        String categoryName = scanner.nextLine();
        try {
            Category category = new Category(null, categoryName);
            categoryService.createCategory(category);
            System.out.println("Category created successfully!");
        } catch (RuntimeException e) {
            System.out.println("Error creating category: " + e.getMessage());
        }
    }

    private void updateCategory(Scanner scanner) {
        List<Category> categories = categoryService.getAllCategories();
        if (categories.isEmpty()) {
            System.out.println("No categories available to update.");
            return;
        }

        System.out.println("Select a category to update:");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i).getCategoryName());
        }

        int categoryIndex = getUserOption(scanner) - 1;
        if (categoryIndex < 0 || categoryIndex >= categories.size()) {
            System.out.println("Invalid category selection.");
            return;
        }

        Category selectedCategory = categories.get(categoryIndex);

        System.out.print("Enter new category name (leave blank to keep current): ");
        String newCategoryName = scanner.nextLine().trim();
        if (!newCategoryName.isEmpty()) {
            selectedCategory.setCategoryName(newCategoryName);
        } else {
            System.out.println("Category name remains unchanged.");
        }

        try {
            categoryService.updateCategory(selectedCategory);
            System.out.println("Category updated successfully!");
        } catch (RuntimeException e) {
            System.out.println("Error updating category: " + e.getMessage());
        }
    }

    private void deleteCategory(Scanner scanner) {
        List<Category> categories = categoryService.getAllCategories();
        if (categories.isEmpty()) {
            System.out.println("No categories available to delete.");
            return;
        }

        System.out.println("Select a category to delete:");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i).getCategoryName());
        }

        int categoryIndex = getUserOption(scanner) - 1;
        if (categoryIndex < 0 || categoryIndex >= categories.size()) {
            System.out.println("Invalid category selection.");
            return;
        }

        Category selectedCategory = categories.get(categoryIndex);

        System.out.print("Are you sure you want to delete the category '" + selectedCategory.getCategoryName() + "'? (yes/no): ");
        String confirm = scanner.nextLine().trim();
        if (confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y")) {
            try {
                categoryService.deleteCategory(selectedCategory.getCategoryId());
                System.out.println("Category deleted successfully.");
            } catch (RuntimeException e) {
                System.out.println("Error deleting category: " + e.getMessage());
            }
        } else {
            System.out.println("Category deletion cancelled.");
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

    private void manageReminders(Scanner scanner) {
        System.out.println("\n=== Manage Reminders ===");
        System.out.println("1. Add Reminder to Task");
        System.out.println("2. View My Reminders");
        System.out.println("3. Mark Reminder as Complete");
        System.out.println("4. Back to Main Menu");

        int option = getUserOption(scanner);
        switch (option) {
            case 1 -> addReminderToTask(scanner);
            case 2 -> viewReminders();
            case 3 -> completeReminder(scanner);
            case 4 -> {
            }
            default -> System.out.println("Invalid option!");
        }
    }

    private void addReminderToTask(Scanner scanner) {
        List<TaskEntity> tasks = taskService.getAllTasksByUser(loggedInUser.getUserId());
        if (tasks.isEmpty()) {
            System.out.println("No tasks available to add a reminder for.");
            return;
        }
        System.out.println("Select a task to add a reminder for:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i).getTaskName());
        }
        int taskIndex = getUserOption(scanner) - 1;
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            System.out.println("Invalid task selection.");
            return;
        }
        TaskEntity selectedTask = tasks.get(taskIndex);

        Date reminderDateTime = inputReminderDateTime(scanner);
        if (reminderDateTime == null) {
            System.out.println("Reminder not added due to invalid input.");
            return;
        }
        Reminder reminder = new Reminder(null, reminderDateTime, selectedTask);
        try {
            reminderService.createReminder(reminder);
            System.out.println("Reminder added successfully!");
        } catch (RuntimeException e) {
            System.out.println("Error adding reminder: " + e.getMessage());
        }
    }

    private void viewReminders() {
        try {
            List<Reminder> reminders = reminderService.getRemindersByUser(loggedInUser.getUserId());
            if (reminders.isEmpty()) {
                System.out.println("No reminders found.");
            } else {
                System.out.println("\n=== Your Reminders ===");
                reminders.forEach(reminder -> System.out.printf(
                        "Reminder ID: %d | Task: %s | DateTime: %s | Completed: %b%n",
                        reminder.getReminderId(),
                        reminder.getTask().getTaskName(),
                        reminder.getReminderDateTime(),
                        reminder.isComplete()
                ));
            }
        } catch (RuntimeException e) {
            System.out.println("Error retrieving reminders: " + e.getMessage());
        }
    }

    private void completeReminder(Scanner scanner) {
        System.out.print("Enter Reminder ID to mark as complete: ");
        int reminderId = getUserOption(scanner);
        try {
            reminderService.markReminderComplete((long) reminderId);
            System.out.println("Reminder marked as complete.");
        } catch (RuntimeException e) {
            System.out.println("Error completing reminder: " + e.getMessage());
        }
    }

    /**
     * Prompts the user to input a reminder date and time.
     * - The user enters a date in dd/MM format (the current year is assumed).
     * - The user then enters time as 4 digits (HHMM) in a 24-hour clock.
     * The colon is auto-inserted (e.g., input 0830 becomes "08:30").
     * - The hour must be between 00 and 23.
     * - The minute must be one of: 00, 15, 30, or 45.
     * <p>
     * Returns a Date object if parsing is successful; otherwise, returns null.
     */
    private Date inputReminderDateTime(Scanner scanner) {
        // Prompt for the date in dd/MM format
        System.out.print("Enter reminder date (dd/MM): ");
        String dateStr = scanner.nextLine().trim();
        if (!dateStr.matches("\\d{2}/\\d{2}")) {
            System.out.println("Invalid date format. Please use dd/MM.");
            return null;
        }

        // Assume current year
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        String fullDateStr = dateStr + "/" + currentYear;  // e.g., "25/12/2025"

        // Prompt for the time as 4 digits (HHMM)
        System.out.print("Enter time (4 digits, HHMM, e.g., 0830 for 08:30): ");
        String timeInput = scanner.nextLine().trim();
        if (!timeInput.matches("\\d{4}")) {
            System.out.println("Invalid time format. Please enter exactly 4 digits.");
            return null;
        }

        // Insert the colon to form HH:MM
        String timeStr = timeInput.substring(0, 2) + ":" + timeInput.substring(2, 4);

        // Validate hour (00-23)
        int hour = Integer.parseInt(timeInput.substring(0, 2));
        if (hour < 0 || hour > 23) {
            System.out.println("Invalid hour value. Must be between 00 and 23.");
            return null;
        }

        // Validate minute: only allow 00, 15, 30, or 45
        String minute = timeInput.substring(2, 4);
        if (!(minute.equals("00") || minute.equals("15") || minute.equals("30") || minute.equals("45"))) {
            System.out.println("Invalid minute value. Allowed minutes are 00, 15, 30, or 45.");
            return null;
        }

        // Combine full date and time string
        String dateTimeStr = fullDateStr + " " + timeStr;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            return sdf.parse(dateTimeStr);
        } catch (ParseException e) {
            System.out.println("Invalid date/time format.");
            return null;
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
