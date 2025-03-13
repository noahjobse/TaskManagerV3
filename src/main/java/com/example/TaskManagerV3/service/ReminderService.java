package com.example.TaskManagerV3.service;

import com.example.TaskManagerV3.model.Reminder;
import com.example.TaskManagerV3.repository.ReminderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReminderService {
    private final ReminderRepository reminderRepository;

    public ReminderService(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    // CREATE: Save a new reminder
    public Reminder createReminder(Reminder reminder) {
        return reminderRepository.save(reminder);
    }

    // READ: Get all reminders for a given user
    public List<Reminder> getRemindersByUser(Long userId) {
        return reminderRepository.findByTask_User_UserId(userId);
    }

    // READ: Get a single reminder by its ID
    public Reminder getReminderById(Long reminderId) {
        return reminderRepository.findById(reminderId)
                .orElseThrow(() -> new RuntimeException("Reminder not found with ID: " + reminderId));
    }

    // UPDATE: Mark a reminder as complete
    public Reminder markReminderComplete(Long reminderId) {
        Reminder reminder = getReminderById(reminderId);
        reminder.markComplete();
        return reminderRepository.save(reminder);
    }

    // DELETE: Remove a reminder by its ID
    public void deleteReminder(Long reminderId) {
        if (!reminderRepository.existsById(reminderId)) {
            throw new RuntimeException("Reminder not found with ID: " + reminderId);
        }
        reminderRepository.deleteById(reminderId);
    }
}
