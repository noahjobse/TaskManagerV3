package com.example.TaskManagerV3.serviceTests;

import com.example.TaskManagerV3.model.User;
import com.example.TaskManagerV3.repository.UserRepository;
import com.example.TaskManagerV3.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setUserId(1L);
        sampleUser.setUserName("testUser");
        sampleUser.setPassword("password123");
    }

    @Test
    void registerUser_ShouldSaveUser_WhenUserDoesNotExist() {
        when(userRepository.existsByUserName("testUser")).thenReturn(false);
        when(userRepository.save(sampleUser)).thenReturn(sampleUser);

        assertDoesNotThrow(() -> userService.registerUser(sampleUser));
        verify(userRepository, times(1)).existsByUserName("testUser");
        verify(userRepository, times(1)).save(sampleUser);
    }

    @Test
    void registerUser_ShouldThrowException_WhenUserAlreadyExists() {
        when(userRepository.existsByUserName("testUser")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.registerUser(sampleUser));
        verify(userRepository, times(1)).existsByUserName("testUser");
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerUser_ShouldThrowException_WhenPasswordIsEmpty() {
        sampleUser.setPassword("");

        assertThrows(RuntimeException.class, () -> userService.registerUser(sampleUser));
        verify(userRepository, never()).save(any());
    }

    @Test
    void authenticate_ShouldReturnUser_WhenCredentialsAreCorrect() {
        when(userRepository.findByUserName("testUser")).thenReturn(Optional.of(sampleUser));

        User result = userService.authenticate("testUser", "password123");

        assertNotNull(result);
        assertEquals(sampleUser, result);
        verify(userRepository, times(1)).findByUserName("testUser");
    }

    @Test
    void authenticate_ShouldThrowException_WhenUserDoesNotExist() {
        when(userRepository.findByUserName("testUser")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.authenticate("testUser", "password123"));
        verify(userRepository, times(1)).findByUserName("testUser");
    }

    @Test
    void authenticate_ShouldThrowException_WhenPasswordIsIncorrect() {
        when(userRepository.findByUserName("testUser")).thenReturn(Optional.of(sampleUser));

        assertThrows(RuntimeException.class, () -> userService.authenticate("testUser", "wrongPassword"));
        verify(userRepository, times(1)).findByUserName("testUser");
    }

    @Test
    void getUserByUserName_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findByUserName("testUser")).thenReturn(Optional.of(sampleUser));

        User result = userService.getUserByUserName("testUser");

        assertNotNull(result);
        assertEquals(sampleUser, result);
        verify(userRepository, times(1)).findByUserName("testUser");
    }

    @Test
    void getUserByUserName_ShouldThrowException_WhenUserDoesNotExist() {
        when(userRepository.findByUserName("testUser")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserByUserName("testUser"));
        verify(userRepository, times(1)).findByUserName("testUser");
    }

    @Test
    void updateUser_ShouldSaveUser_WhenUserExists() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.save(sampleUser)).thenReturn(sampleUser);

        User result = userService.updateUser(sampleUser);

        assertNotNull(result);
        assertEquals(sampleUser, result);
        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).save(sampleUser);
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserDoesNotExist() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userService.updateUser(sampleUser));
        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_ShouldDeleteUser_WhenUserExists() {
        when(userRepository.findByUserName("testUser")).thenReturn(Optional.of(sampleUser));
        doNothing().when(userRepository).delete(sampleUser);

        assertDoesNotThrow(() -> userService.deleteUser("testUser"));
        verify(userRepository, times(1)).findByUserName("testUser");
        verify(userRepository, times(1)).delete(sampleUser);
    }

    @Test
    void deleteUser_ShouldThrowException_WhenUserDoesNotExist() {
        when(userRepository.findByUserName("testUser")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.deleteUser("testUser"));
        verify(userRepository, times(1)).findByUserName("testUser");
        verify(userRepository, never()).delete(any());
    }
}
