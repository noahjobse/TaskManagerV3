package com.example.TaskManagerV3.repository;

import com.example.TaskManagerV3.model.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    // Retrieve all reminders associated with a given user's tasks, eagerly fetching the task
    @Query("SELECT r FROM Reminder r JOIN FETCH r.task WHERE r.task.user.userId = :userId")
    List<Reminder> findByTask_User_UserId(@Param("userId") Long userId);

    // Retrieve all reminders that are not yet complete, eagerly fetching the task
    @Query("SELECT r FROM Reminder r JOIN FETCH r.task WHERE r.isComplete = false")
    List<Reminder> findByIsCompleteFalse();
}
