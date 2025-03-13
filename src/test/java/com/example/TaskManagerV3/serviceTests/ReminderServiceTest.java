package com.example.TaskManagerV3.serviceTests;

import com.example.TaskManagerV3.model.Reminder;
import com.example.TaskManagerV3.repository.ReminderRepository;
import com.example.TaskManagerV3.service.ReminderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReminderServiceTest {

    @Mock
    private ReminderRepository reminderRepository;

    @InjectMocks
    private ReminderService reminderService;

    private Reminder sampleReminder;

    @BeforeEach
    void setUp() {
        sampleReminder = new Reminder();
        sampleReminder.setReminderId(1L);
        sampleReminder.markComplete();  // ✅ If method is parameterless
    }

    @Test
    void createReminder_ShouldSaveReminder() {
        when(reminderRepository.save(sampleReminder)).thenReturn(sampleReminder);

        Reminder result = reminderService.createReminder(sampleReminder);

        assertNotNull(result);
        assertEquals(sampleReminder, result);
        verify(reminderRepository, times(1)).save(sampleReminder);
    }

    @Test
    void getRemindersByUser_ShouldReturnReminderList_WhenUserIdExists() {
        List<Reminder> reminders = List.of(sampleReminder);
        when(reminderRepository.findByTask_User_UserId(1L)).thenReturn(reminders);

        List<Reminder> result = reminderService.getRemindersByUser(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(sampleReminder, result.getFirst());
        verify(reminderRepository, times(1)).findByTask_User_UserId(1L);
    }

    @Test
    void getReminderById_ShouldReturnReminder_WhenIdExists() {
        when(reminderRepository.findById(1L)).thenReturn(Optional.of(sampleReminder));

        Reminder result = reminderService.getReminderById(1L);

        assertNotNull(result);
        assertEquals(sampleReminder, result);
        verify(reminderRepository, times(1)).findById(1L);
    }

    @Test
    void getReminderById_ShouldThrowException_WhenIdDoesNotExist() {
        when(reminderRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> reminderService.getReminderById(2L));
        verify(reminderRepository, times(1)).findById(2L);
    }

    @Test
    void markReminderComplete_ShouldSetCompletedAndSave_WhenReminderExists() {
        when(reminderRepository.findById(1L)).thenReturn(Optional.of(sampleReminder));
        when(reminderRepository.save(sampleReminder)).thenReturn(sampleReminder);

        Reminder result = reminderService.markReminderComplete(1L);

        assertNotNull(result);
        assertTrue(result.isComplete());
        verify(reminderRepository, times(1)).findById(1L);
        verify(reminderRepository, times(1)).save(sampleReminder);
    }

    @Test
    void markReminderComplete_ShouldThrowException_WhenReminderDoesNotExist() {
        when(reminderRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> reminderService.markReminderComplete(2L));
        verify(reminderRepository, times(1)).findById(2L);
        verify(reminderRepository, never()).save(any());
    }

    @Test
    void deleteReminder_ShouldDeleteReminder_WhenIdExists() {
        when(reminderRepository.existsById(1L)).thenReturn(true);
        doNothing().when(reminderRepository).deleteById(1L);

        assertDoesNotThrow(() -> reminderService.deleteReminder(1L));
        verify(reminderRepository, times(1)).existsById(1L);
        verify(reminderRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteReminder_ShouldThrowException_WhenIdDoesNotExist() {
        when(reminderRepository.existsById(2L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> reminderService.deleteReminder(2L));
        verify(reminderRepository, times(1)).existsById(2L);
        verify(reminderRepository, never()).deleteById(any());
    }
}
