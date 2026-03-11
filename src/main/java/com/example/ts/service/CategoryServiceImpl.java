package com.example.ts.service;

import com.example.ts.domain.Category;
import com.example.ts.dto.CategoryDto;
import com.example.ts.mapper.CategoryMapper;
import com.example.ts.repository.CategoryRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository repository;
  private final CategoryMapper mapper;

  public CategoryServiceImpl(CategoryRepository repository, CategoryMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public List<CategoryDto> getAllCategories() {
    return repository.findAll()
        .stream()
        .map(mapper::toDto)
        .toList();
  }

  @Override
  public CategoryDto getCategoryById(Long id) {

    Category category = repository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Category not found"));

    return mapper.toDto(category);
  }

  @Override
  public CategoryDto createCategory(CategoryDto dto) {

    Category category = mapper.toEntity(dto);

    return mapper.toDto(repository.save(category));
  }

  @Override
  public CategoryDto updateCategory(Long id, CategoryDto dto) {

    Category category = repository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Category not found"));

    category.setName(dto.getName());

    return mapper.toDto(repository.save(category));
  }

  @Override
  public void deleteCategory(Long id) {
    repository.deleteById(id);
  }

}