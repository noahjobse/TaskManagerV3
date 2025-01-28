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


    public void registerUser(User user) {
        // Check if the username already exists
        if (userRepository.existsByUserName(user.getUserName())) {
            throw new RuntimeException("User already exists!");
        }
        // Check for empty or null password
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Password cannot be empty!");
        }
        // Save the user
        userRepository.save(user);
    }


    public User authenticate(String username, String password) {
        Optional<User> optionalUser = userRepository.findByUserName(username);

        // Verify username exists and password matches
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getPassword().equals(password)) {
                return user;
            }
        }
        throw new RuntimeException("Invalid username or password!");
    }


    public void deleteUser(String username) {
        Optional<User> optionalUser = userRepository.findByUserName(username);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found!");
        }
        userRepository.delete(optionalUser.get());
    }
}
