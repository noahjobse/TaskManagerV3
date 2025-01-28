package com.example.TaskManagerV3.service;

import com.example.TaskManagerV3.model.Reminder;
import com.example.TaskManagerV3.repository.ReminderRepository;
import org.springframework.stereotype.Service;

@Service
public class ReminderService {
    private final ReminderRepository reminderRepository;

    public ReminderService(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    public void createReminder(Reminder reminder) {
        reminderRepository.save(reminder);
    }
}