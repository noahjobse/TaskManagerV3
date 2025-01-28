package com.example.TaskManagerV3.service;

import com.example.TaskManagerV3.model.TaskEntity;
import com.example.TaskManagerV3.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // Create or Update a Task
    public TaskEntity createOrUpdateTask(TaskEntity task) {
        return taskRepository.save(task);
    }

    // Get All Tasks
    public Iterable<TaskEntity> getAllTasks() {
        return taskRepository.findAll();
    }

    // Get All Tasks by User ID
    public List<TaskEntity> getAllTasksByUser(Long userId) {
        return taskRepository.findByUser_UserId(userId);
    }

    // Get Task by ID
    public TaskEntity getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + id));
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
    public boolean existsById(Long id) {
        return taskRepository.existsById(id);
    }

    // Find Tasks by Status (Custom Query Example)
    public Iterable<TaskEntity> findTasksByStatus(String status) {
        return taskRepository.findByStatus(status);
    }

    // Find Tasks by User ID (Custom Query Example)
    public Iterable<TaskEntity> findTasksByUserId(Long userId) {
        return taskRepository.findByUser_UserId(userId);
    }
}
