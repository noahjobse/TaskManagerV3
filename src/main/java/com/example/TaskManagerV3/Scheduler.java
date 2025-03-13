package com.example.TaskManagerV3;

import com.example.TaskManagerV3.model.Reminder;
import com.example.TaskManagerV3.service.ReminderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

@Component
public class Scheduler {

    private final ReminderService reminderService;

    public Scheduler(ReminderService reminderService) {
        this.reminderService = reminderService;
    }
}
