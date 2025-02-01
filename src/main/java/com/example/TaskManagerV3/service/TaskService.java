package com.example.TaskManagerV3.service;

import com.example.TaskManagerV3.model.TaskEntity;
import com.example.TaskManagerV3.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional // Ensures transactions are active for the methods in this class
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

// CREATE: Create a new task
public TaskEntity createTask(TaskEntity task) {
    if (task.getCategory() == null) {
        throw new RuntimeException("Task must have a valid category assigned.");
    }
    return taskRepository.save(task);
}

    // READ: Retrieve all tasks by user ID
    @Transactional(readOnly = true)
    public List<TaskEntity> getAllTasksByUser(Long userId) {
        List<TaskEntity> tasks = taskRepository.findByUser_UserId(userId);
        tasks.forEach(this::initializeCategory);
        return tasks;
    }

    // READ: Retrieve a task by its ID
    @Transactional(readOnly = true)
    public TaskEntity getTaskById(Long id) {
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + id));
        return initializeCategory(task);
    }

    // UPDATE: Update an existing task (alias for createOrUpdateTask)
    public TaskEntity updateTask(TaskEntity task) {
        if (task.getTaskId() == null || !taskRepository.existsById(task.getTaskId())) {
            throw new RuntimeException("Task not found with ID: " + task.getTaskId());
        }
        return taskRepository.save(task);
    }

    // READ: Retrieve a task with its reminders by its ID
    @Transactional
    public TaskEntity getTaskWithReminders(Long taskId) {
        return taskRepository.findTaskWithReminders(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    // DELETE: Delete a task by its ID
    public void deleteTaskById(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
        } else {
            throw new RuntimeException("Task not found with ID: " + id);
        }
    }

    // Helper method to initialize the category to prevent LazyInitializationException
    private TaskEntity initializeCategory(TaskEntity task) {
        if (task.getCategory() != null) {
            task.getCategory().getCategoryName(); // Force initialization of lazy property
        } else {
            System.out.println("Warning: Task ID " + task.getTaskId() + " has no category assigned.");
        }
        return task;
    }
}
