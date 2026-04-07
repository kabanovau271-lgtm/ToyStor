package com.example.ts.controller;

import com.example.ts.dto.CategoryDto;
import com.example.ts.service.CategoryService;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Категории", description = "API для управления категориями")
@RestController
@RequestMapping("/categories")
public class CategoryController {

  private final CategoryService service;

  public CategoryController(CategoryService service) {
    this.service = service;
  }

  @Operation(summary = "Получить все категории")
  @ApiResponse(responseCode = "200", description = "Список категорий получен")
  @GetMapping
  public List<CategoryDto> getAllCategories() {
    return service.getAllCategories();
  }

  @Operation(summary = "Получить категорию по ID")
  @ApiResponse(responseCode = "200", description = "Категория найдена")
  @ApiResponse(responseCode = "404", description = "Категория не найдена")
  @GetMapping("/{id}")
  public CategoryDto getCategoryById(@PathVariable Long id) {
    return service.getCategoryById(id);
  }

  @Operation(summary = "Создать категорию")
  @ApiResponse(responseCode = "200", description = "Категория создана")
  @ApiResponse(responseCode = "400", description = "Ошибка валидации")
  @PostMapping
  public CategoryDto createCategory(@RequestBody CategoryDto dto) {
    return service.createCategory(dto);
  }

  @Operation(summary = "Обновить категорию")
  @ApiResponse(responseCode = "200", description = "Категория обновлена")
  @ApiResponse(responseCode = "404", description = "Категория не найдена")
  @PutMapping("/{id}")
  public CategoryDto updateCategory(@PathVariable Long id,
                                    @RequestBody CategoryDto dto) {
    return service.updateCategory(id, dto);
  }

  @Operation(summary = "Удалить категорию")
  @ApiResponse(responseCode = "204", description = "Категория удалена")
  @ApiResponse(responseCode = "404", description = "Категория не найдена")
  @DeleteMapping("/{id}")
  public void deleteCategory(@PathVariable Long id) {
    service.deleteCategory(id);
  }
}