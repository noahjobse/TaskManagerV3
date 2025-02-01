package com.example.TaskManagerV3.repository;

import com.example.TaskManagerV3.model.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    List<TaskEntity> findByUser_UserId(Long userId);

    List<TaskEntity> findByStatus(String status);

    @Query("SELECT t FROM TaskEntity t LEFT JOIN FETCH t.reminders WHERE t.taskId = :taskId")
    Optional<TaskEntity> findTaskWithReminders(@Param("taskId") Long taskId);

}
