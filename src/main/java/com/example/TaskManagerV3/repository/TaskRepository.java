package com.example.TaskManagerV3.repository;

import com.example.TaskManagerV3.model.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    List<TaskEntity> findByUser_UserId(Long userId);

    List<TaskEntity> findByStatus(String status);
}
