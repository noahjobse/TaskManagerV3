package com.example.TaskManagerV3.repository;

import com.example.TaskManagerV3.model.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReminderRepository extends JpaRepository<Reminder, Integer> {}