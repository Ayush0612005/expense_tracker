package com.ayush.expensetracker.service;

import com.ayush.expensetracker.dto.category.CategoryRequest;
import com.ayush.expensetracker.dto.category.CategoryResponse;
import com.ayush.expensetracker.entity.Category;
import com.ayush.expensetracker.entity.User;
import com.ayush.expensetracker.exception.CategoryAlreadyExistsException;
import com.ayush.expensetracker.exception.ResourceNotFoundException;
import com.ayush.expensetracker.mapper.CategoryMapper;
import com.ayush.expensetracker.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    @CacheEvict(
            value = {
                    "dashboard",
                    "summary",
                    "monthlyReport",
                    "categoryReport"
            },
            allEntries = true
    )
    public CategoryResponse create(CategoryRequest request) {

        User user = getCurrentUser();

        if (categoryRepository.existsByNameAndUser(request.getName(), user)) {
            throw new CategoryAlreadyExistsException("Category already exists.");
        }

        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .user(user)
                .build();

        Category savedCategory = categoryRepository.save(category);

        return CategoryMapper.toResponse(savedCategory);
    }

    public List<CategoryResponse> getAll() {

        User user = getCurrentUser();

        return categoryRepository.findByUser(user)
                .stream()
                .map(CategoryMapper::toResponse)
                .toList();
    }

    public CategoryResponse getById(Long id) {

        User user = getCurrentUser();

        Category category = categoryRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

        return CategoryMapper.toResponse(category);
    }

    @CacheEvict(
            value = {
                    "dashboard",
                    "summary",
                    "monthlyReport",
                    "categoryReport"
            },
            allEntries = true
    )
    public CategoryResponse update(Long id, CategoryRequest request) {

        User user = getCurrentUser();

        Category category = categoryRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

        if (!category.getName().equalsIgnoreCase(request.getName())
                && categoryRepository.existsByNameAndUser(request.getName(), user)) {

            throw new CategoryAlreadyExistsException("Category already exists.");
        }

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category updatedCategory = categoryRepository.save(category);

        return CategoryMapper.toResponse(updatedCategory);
    }

    @CacheEvict(
            value = {
                    "dashboard",
                    "summary",
                    "monthlyReport",
                    "categoryReport"
            },
            allEntries = true
    )
    public void delete(Long id) {

        User user = getCurrentUser();

        Category category = categoryRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

        categoryRepository.delete(category);
    }
}
