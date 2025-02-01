# Task Manager

## Overview
The **Task Manager** is a comprehensive application designed to streamline task management, reminders, notifications, and analytics for professionals. This version focuses on the backend, which has been restructured as a **console application** to test and develop functionality before transitioning to a full stack application with a React-based frontend.

---

## Architecture
The application adheres to the **Model-View-Controller-Service (MVCS)** architecture pattern to maintain separation of concerns and ensure modularity:

1. **Model Layer**: Represents the data and business logic, including entities such as `Task`, `Category`, `User`, `Reminder`, and `Notification`.
2. **View Layer**: (Console Application) Provides interaction with users through a command-line interface.
3. **Controller Layer**: Handles user input from the console and routes it to the appropriate services.
4. **Service Layer**: Contains business logic and handles interactions between controllers and the data access layer.
5. **Data Access Layer**: Manages database operations via in-memory storage (for testing purposes) or PostgreSQL in future iterations.

---

## Features
### Authentication and Authorization
- **Role-Based Access Control**:
  - `Admin`: Full control over users, tasks, and categories.
  - `Regular User`: Manage personal tasks, reminders, and notifications.
- **Secure Login System**:
  - User registration and login.
  - Passwords are securely hashed using bcrypt.

### Task Management
- **CRUD Operations**:
  - Create, update, retrieve, and delete tasks.
- **Attributes**:
  - Priority, status, due date, description, and category associations.

### Category Management
- Create, update, delete, and retrieve task categories.
- Tasks can belong to multiple categories.

### Reminders and Notifications
- **Reminders**:
  - Set reminders for tasks with recurrence and snoozing options.
- **Notifications**:
  - Manage in-app alerts and test email-based notifications.

### Audit Logs
- Logs user actions (e.g., login, task updates, deletions) for auditing and analytics.

### Reports and Analytics
- Generate summaries of tasks, such as:
  - Pending tasks.
  - Category breakdown.

---

## Console Application Details
### Purpose
The console application serves as a testing and development environment for the backend. It allows functionality to be validated through direct interaction with the core features.

### Functionality
- **Menu System**:
  - Navigable menu to perform operations on tasks, categories, reminders, and notifications.
- **Testing Features**:
  - Console commands to validate business logic, database interactions, and service/controller functionality.
- **User Interaction**:
  - Input validation and error handling to ensure robust operations.

---

## Tech Stack
- **Java**: Core programming language.
- **Spring Boot**: Framework for backend development (modular structure maintained for future web app).
- **JUnit**: Used for unit and integration testing.
- **PostgreSQL**: Database (in-memory for now; PostgreSQL planned for deployment).
- **Maven**: Dependency and project management.

---

## How to Run the Console App
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/ognoah17/TaskManager2.git
   cd TaskManager2
   ```

2. **Build the Application**:
   ```bash
   mvn clean install
   ```

3. **Run the Console App**:
   ```bash
   java -jar target/task-manager-1.0-SNAPSHOT.jar
   ```

4. **Follow Console Prompts**:
   - Use the menu system to interact with tasks, categories, and reminders.

---

## Planned Features
### Frontend
- **React with create-react-app**:
  - User-friendly web interface for task management, reminders, and analytics.
  - Mobile responsiveness and integration with backend APIs.

### Chatbot Integration
- A chatbot UI powered by GPT-3.5 for natural language interaction:
  - Create, update, and retrieve tasks.
  - Set reminders and manage notifications conversationally.
  - Generate reports through simple text/voice commands.

### Deployment
- **Backend**: Hosted on Heroku.
- **Frontend**: Hosted on Netlify.
- **Containerization**: Docker for consistency across environments.
- **CI/CD**: GitHub Actions for automated builds and testing.

---

## Testing and Validation
### Unit and Integration Tests
- Tested with JUnit to ensure:
  - Proper interaction between layers.
  - Validation of business rules and database operations.

### Manual Testing
- **Console Commands**: Simulated real-world operations via the CLI.
- **Postman**: Used previously to test API endpoints in the web-based version.
