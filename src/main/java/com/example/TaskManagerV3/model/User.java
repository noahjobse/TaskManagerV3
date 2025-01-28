package com.example.TaskManagerV3.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId; // Use Long for consistency with other entities like TaskEntity

    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "user_role", nullable = false)
    private String userRole;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<TaskEntity> tasks; // Bidirectional relationship with TaskEntity

    // Constructor with all fields except collections
    public User(Long userId, String userName, String password, String userRole) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.userRole = userRole;
    }

    // Default no-argument constructor (required by JPA)
    public User() {
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public Set<TaskEntity> getTasks() {
        return tasks;
    }

    public void setTasks(Set<TaskEntity> tasks) {
        this.tasks = tasks;
    }

    // Utility method to check if user is admin
    public boolean isAdmin() {
        return "Admin".equalsIgnoreCase(userRole);
    }
}
