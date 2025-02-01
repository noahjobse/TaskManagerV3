package com.example.TaskManagerV3.service;

import com.example.TaskManagerV3.model.User;
import com.example.TaskManagerV3.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    // Constructor injection
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // CREATE: Register a new user
    public void registerUser(User user) {
        if (userRepository.existsByUserName(user.getUserName())) {
            throw new RuntimeException("User already exists!");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Password cannot be empty!");
        }
        userRepository.save(user);
    }

    // READ: Authenticate and retrieve a user by username and password
    public User authenticate(String username, String password) {
        Optional<User> optionalUser = userRepository.findByUserName(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getPassword().equals(password)) {
                return user;
            }
        }
        throw new RuntimeException("Invalid username or password!");
    }

    // READ: Retrieve a user by their username
    public User getUserByUserName(String username) {
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    // UPDATE: Update an existing user (for example, change password or other details)
    public User updateUser(User user) {
        if (user.getUserId() == null || !userRepository.existsById(user.getUserId())) {
            throw new RuntimeException("User not found with ID: " + user.getUserId());
        }
        return userRepository.save(user);
    }

    // DELETE: Delete a user by username
    public void deleteUser(String username) {
        Optional<User> optionalUser = userRepository.findByUserName(username);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found!");
        }
        userRepository.delete(optionalUser.get());
    }
}
