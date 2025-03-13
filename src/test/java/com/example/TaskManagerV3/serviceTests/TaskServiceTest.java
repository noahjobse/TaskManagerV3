package com.example.TaskManagerV3.serviceTests;

import com.example.TaskManagerV3.model.Category;
import com.example.TaskManagerV3.model.TaskEntity;
import com.example.TaskManagerV3.repository.TaskRepository;
import com.example.TaskManagerV3.service.TaskService;
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
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private TaskEntity sampleTask;

    @BeforeEach
    void setUp() {
        sampleTask = new TaskEntity();
        sampleTask.setTaskId(1L);
        sampleTask.setCategory(new Category(1L, "Work"));  // Assuming Category has an ID and name
    }

    @Test
    void createTask_ShouldSaveTask_WhenCategoryIsValid() {
        when(taskRepository.save(sampleTask)).thenReturn(sampleTask);

        TaskEntity result = taskService.createTask(sampleTask);

        assertNotNull(result);
        assertEquals(sampleTask, result);
        verify(taskRepository, times(1)).save(sampleTask);
    }

    @Test
    void createTask_ShouldThrowException_WhenCategoryIsNull() {
        sampleTask.setCategory(null);

        assertThrows(RuntimeException.class, () -> taskService.createTask(sampleTask));
        verify(taskRepository, never()).save(any());
    }

    @Test
    void getAllTasksByUser_ShouldReturnTaskList_WhenUserIdExists() {
        List<TaskEntity> tasks = List.of(sampleTask);
        when(taskRepository.findByUser_UserId(1L)).thenReturn(tasks);

        List<TaskEntity> result = taskService.getAllTasksByUser(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(sampleTask, result.getFirst());
        verify(taskRepository, times(1)).findByUser_UserId(1L);
    }

    @Test
    void getTaskById_ShouldReturnTask_WhenIdExists() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));

        TaskEntity result = taskService.getTaskById(1L);

        assertNotNull(result);
        assertEquals(sampleTask, result);
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void getTaskById_ShouldThrowException_WhenIdDoesNotExist() {
        when(taskRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.getTaskById(2L));
        verify(taskRepository, times(1)).findById(2L);
    }

    @Test
    void updateTask_ShouldSaveTask_WhenTaskExists() {
        when(taskRepository.existsById(1L)).thenReturn(true);
        when(taskRepository.save(sampleTask)).thenReturn(sampleTask);

        TaskEntity result = taskService.updateTask(sampleTask);

        assertNotNull(result);
        assertEquals(sampleTask, result);
        verify(taskRepository, times(1)).existsById(1L);
        verify(taskRepository, times(1)).save(sampleTask);
    }

    @Test
    void updateTask_ShouldThrowException_WhenTaskDoesNotExist() {
        when(taskRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> taskService.updateTask(sampleTask));
        verify(taskRepository, times(1)).existsById(1L);
        verify(taskRepository, never()).save(any());
    }

    @Test
    void deleteTaskById_ShouldDeleteTask_WhenIdExists() {
        when(taskRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(1L);

        assertDoesNotThrow(() -> taskService.deleteTaskById(1L));
        verify(taskRepository, times(1)).existsById(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTaskById_ShouldThrowException_WhenIdDoesNotExist() {
        when(taskRepository.existsById(2L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> taskService.deleteTaskById(2L));
        verify(taskRepository, times(1)).existsById(2L);
        verify(taskRepository, never()).deleteById(any());
    }
}
