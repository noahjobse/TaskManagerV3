package com.example.TaskManagerV3.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reminders")
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reminder_id")
    private Long reminderId;

    @Column(name = "reminder_date_time", nullable = false)
    private Date reminderDateTime;

    @Column(name = "is_complete")
    private boolean isComplete;

    @Column(name = "is_recurring")
    private boolean recurring;

    @Column(name = "repeat_frequency")
    private String repeatFrequency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private TaskEntity task;

    public Reminder() {
    }

    public Reminder(Long reminderId, Date reminderDateTime, TaskEntity task) {
        this.reminderId = reminderId;
        this.reminderDateTime = reminderDateTime;
        this.task = task;
        this.isComplete = false;
        this.recurring = false;
    }

    // Getters and methods

    public Long getReminderId() {
        return reminderId;
    }

    public Date getReminderDateTime() {
        return reminderDateTime;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public String getRepeatFrequency() {
        return repeatFrequency;
    }

    public TaskEntity getTask() {
        return task;
    }

    public void setTask(TaskEntity task) {
        this.task = task;
    }

    public void markComplete() {
        this.isComplete = true;
    }

    public void setRecurring(String frequency) {
        this.recurring = true;
        this.repeatFrequency = frequency;
    }

    public void clearRecurring() {
        this.recurring = false;
        this.repeatFrequency = null;
    }
}
