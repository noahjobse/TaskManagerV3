package com.example.TaskManagerV3.repository;

import com.example.TaskManagerV3.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsById(Long id);
}