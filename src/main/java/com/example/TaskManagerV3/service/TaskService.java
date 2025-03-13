package com.example.TaskManagerV3.service;

import com.example.TaskManagerV3.model.TaskEntity;
import com.example.TaskManagerV3.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // CREATE: Save a new task
    public TaskEntity createTask(TaskEntity task) {
        if (task.getCategory() == null) {
            throw new RuntimeException("Task must have a valid category assigned.");
        }
        return taskRepository.save(task);
    }

    // READ: Retrieve all tasks for a given user
    @Transactional(readOnly = true)
    public List<TaskEntity> getAllTasksByUser(Long userId) {
        return taskRepository.findByUser_UserId(userId);
    }

    // READ: Retrieve a single task by its ID
    @Transactional(readOnly = true)
    public TaskEntity getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + id));
    }

    // UPDATE: Update an existing task
    public TaskEntity updateTask(TaskEntity task) {
        if (task.getTaskId() == null || !taskRepository.existsById(task.getTaskId())) {
            throw new RuntimeException("Task not found with ID: " + task.getTaskId());
        }
        return taskRepository.save(task);
    }

    // DELETE: Remove a task by its ID
    public void deleteTaskById(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found with ID: " + id);
        }
        taskRepository.deleteById(id);
    }
}
