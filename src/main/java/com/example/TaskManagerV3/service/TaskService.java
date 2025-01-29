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

    // Create or Update a Task
    public TaskEntity createOrUpdateTask(TaskEntity task) {
        if (task.getCategory() == null) {
            throw new RuntimeException("Task must have a valid category assigned.");
        }
        return taskRepository.save(task);
    }

    // Get All Tasks
    @Transactional(readOnly = true)
    public Iterable<TaskEntity> getAllTasks() {
        Iterable<TaskEntity> tasks = taskRepository.findAll();
        tasks.forEach(this::initializeCategory);
        return tasks;
    }

    // Get All Tasks by User ID
    @Transactional(readOnly = true)
    public List<TaskEntity> getAllTasksByUser(Long userId) {
        List<TaskEntity> tasks = taskRepository.findByUser_UserId(userId);
        tasks.forEach(this::initializeCategory);
        return tasks;
    }

    // Get Task by ID
    @Transactional(readOnly = true)
    public TaskEntity getTaskById(Long id) {
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + id));
        return initializeCategory(task);
    }

    // Delete Task by ID
    public void deleteTaskById(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
        } else {
            throw new RuntimeException("Task not found with ID: " + id);
        }
    }

    // Check if Task Exists by ID
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return taskRepository.existsById(id);
    }

    // Find Tasks by Status (Custom Query Example)
    @Transactional(readOnly = true)
    public Iterable<TaskEntity> findTasksByStatus(String status) {
        Iterable<TaskEntity> tasks = taskRepository.findByStatus(status);
        tasks.forEach(this::initializeCategory);
        return tasks;
    }

    // Find Tasks by User ID (Custom Query Example)
    @Transactional(readOnly = true)
    public Iterable<TaskEntity> findTasksByUserId(Long userId) {
        Iterable<TaskEntity> tasks = taskRepository.findByUser_UserId(userId);
        tasks.forEach(this::initializeCategory);
        return tasks;
    }

    // Initialize the category to prevent LazyInitializationException
    private TaskEntity initializeCategory(TaskEntity task) {
        if (task.getCategory() != null) {
            // Explicitly accessing a property on the category to force initialization
            task.getCategory().getCategoryName();
        } else {
            System.out.println("Warning: Task ID " + task.getTaskId() + " has no category assigned.");
        }
        return task;
    }
}
