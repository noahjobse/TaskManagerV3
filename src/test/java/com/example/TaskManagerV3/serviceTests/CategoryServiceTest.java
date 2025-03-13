package com.example.TaskManagerV3.serviceTests;

import com.example.TaskManagerV3.model.Category;
import com.example.TaskManagerV3.repository.CategoryRepository;
import com.example.TaskManagerV3.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category sampleCategory;

    @BeforeEach
    void setUp() {
        sampleCategory = new Category();
        sampleCategory.setCategoryId(1L);
        sampleCategory.setCategoryName("Work");
    }

    @Test
    void createCategory_ShouldSaveCategory() {
        when(categoryRepository.save(sampleCategory)).thenReturn(sampleCategory);

        Category result = categoryService.createCategory(sampleCategory);

        assertNotNull(result);
        assertEquals(sampleCategory, result);
        verify(categoryRepository, times(1)).save(sampleCategory);
    }

    @Test
    void updateCategory_ShouldSaveCategory_WhenCategoryExists() {
        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(categoryRepository.save(sampleCategory)).thenReturn(sampleCategory);

        Category result = categoryService.updateCategory(sampleCategory);

        assertNotNull(result);
        assertEquals(sampleCategory, result);
        verify(categoryRepository, times(1)).existsById(1L);
        verify(categoryRepository, times(1)).save(sampleCategory);
    }

    @Test
    void updateCategory_ShouldThrowException_WhenCategoryDoesNotExist() {
        when(categoryRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> categoryService.updateCategory(sampleCategory));
        verify(categoryRepository, times(1)).existsById(1L);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void deleteCategory_ShouldDeleteCategory_WhenCategoryExists() {
        when(categoryRepository.existsById(1L)).thenReturn(true);
        doNothing().when(categoryRepository).deleteById(1L);

        assertDoesNotThrow(() -> categoryService.deleteCategory(1L));
        verify(categoryRepository, times(1)).existsById(1L);
        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteCategory_ShouldThrowException_WhenCategoryDoesNotExist() {
        when(categoryRepository.existsById(2L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> categoryService.deleteCategory(2L));
        verify(categoryRepository, times(1)).existsById(2L);
        verify(categoryRepository, never()).deleteById(any());
    }

    @Test
    void getAllCategories_ShouldReturnCategoryList() {
        List<Category> categories = List.of(sampleCategory);
        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> result = categoryService.getAllCategories();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(sampleCategory, result.getFirst());
        verify(categoryRepository, times(1)).findAll();
    }
}
