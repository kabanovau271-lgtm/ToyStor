package com.example.ts.controller;

import com.example.ts.dto.CategoryDto;
import com.example.ts.service.CategoryService;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategoryController {

  private final CategoryService service;

  public CategoryController(CategoryService service) {
    this.service = service;
  }

  @GetMapping
  public List<CategoryDto> getAllCategories() {
    return service.getAllCategories();
  }

  @GetMapping("/{id}")
  public CategoryDto getCategoryById(@PathVariable Long id) {
    return service.getCategoryById(id);
  }

  @PostMapping
  public CategoryDto createCategory(@RequestBody CategoryDto dto) {
    return service.createCategory(dto);
  }

  @PutMapping("/{id}")
  public CategoryDto updateCategory(@PathVariable Long id,
                                    @RequestBody CategoryDto dto) {
    return service.updateCategory(id, dto);
  }

  @DeleteMapping("/{id}")
  public void deleteCategory(@PathVariable Long id) {
    service.deleteCategory(id);
  }
}