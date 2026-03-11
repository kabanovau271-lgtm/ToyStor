package com.example.ts.mapper;

import com.example.ts.domain.Category;
import com.example.ts.dto.CategoryDto;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

  public CategoryDto toDto(Category category) {

    CategoryDto dto = new CategoryDto();

    dto.setId(category.getId());
    dto.setName(category.getName());

    return dto;
  }

  public Category toEntity(CategoryDto dto) {

    Category category = new Category();

    category.setId(dto.getId());
    category.setName(dto.getName());

    return category;
  }

}