package com.example.TaskManagerV3.repository;

import com.example.TaskManagerV3.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Check if a username already exists (used during registration)
    boolean existsByUserName(String userName);

    // Find a user by username and password (used during login)
    Optional<User> findByUserNameAndPassword(String userName, String password);

    // Find a user by username only
    Optional<User> findByUserName(String userName);
}
