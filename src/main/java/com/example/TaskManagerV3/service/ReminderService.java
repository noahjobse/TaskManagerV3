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

    // CREATE: Create a new reminder
    public void createReminder(Reminder reminder) {
        reminderRepository.save(reminder);
    }

    // READ: Retrieve all reminders for a given user's tasks
    public List<Reminder> getRemindersByUser(Long userId) {
        return reminderRepository.findByTask_User_UserId(userId);
    }

    // READ: Retrieve all pending (not completed) reminders
    public List<Reminder> getAllPendingReminders() {
        return reminderRepository.findByIsCompleteFalse();
    }

    // READ: Retrieve a reminder by its ID
    public Reminder getReminderById(Long reminderId) {
        return reminderRepository.findById(reminderId)
                .orElseThrow(() -> new RuntimeException("Reminder not found with ID: " + reminderId));
    }

    // UPDATE: Mark a reminder as complete (a specific update)
    public Reminder markReminderComplete(Long reminderId) {
        Reminder reminder = getReminderById(reminderId);
        reminder.markComplete();
        return reminderRepository.save(reminder);
    }

    // UPDATE: General update for a reminder (for example, rescheduling)
    public Reminder updateReminder(Reminder reminder) {
        if (reminder.getReminderId() == null || !reminderRepository.existsById(reminder.getReminderId())) {
            throw new RuntimeException("Reminder not found with ID: " + reminder.getReminderId());
        }
        return reminderRepository.save(reminder);
    }

    // DELETE: Delete a reminder by its ID
    public void deleteReminder(Long reminderId) {
        if (reminderRepository.existsById(reminderId)) {
            reminderRepository.deleteById(reminderId);
        } else {
            throw new RuntimeException("Reminder not found with ID: " + reminderId);
        }
    }
}
