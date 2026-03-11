package com.example.ts.service;

import com.example.ts.dto.CategoryDto;
import java.util.List;

public interface CategoryService {

  List<CategoryDto> getAllCategories();

  CategoryDto getCategoryById(Long id);

  CategoryDto createCategory(CategoryDto dto);

  CategoryDto updateCategory(Long id, CategoryDto dto);

  void deleteCategory(Long id);
}

