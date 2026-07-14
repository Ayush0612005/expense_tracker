package com.ayush.expensetracker.mapper;

import com.ayush.expensetracker.dto.category.CategoryResponse;
import com.ayush.expensetracker.entity.Category;

public class CategoryMapper {

    private CategoryMapper() {
    }

    public static CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }
}

