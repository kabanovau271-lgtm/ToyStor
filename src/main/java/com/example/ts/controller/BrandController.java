package com.example.ts.controller;

import com.example.ts.dto.BrandDto;
import com.example.ts.service.BrandService;
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

@Tag(name = "Бренды", description = "API для управления брендами")
@RestController
@RequestMapping("/brands")
public class BrandController {

  private final BrandService service;

  public BrandController(BrandService service) {
    this.service = service;
  }

  @Operation(summary = "Получить все бренды")
  @ApiResponse(responseCode = "200", description = "Список брендов получен")
  @GetMapping
  public List<BrandDto> getAllBrands() {
    return service.getAllBrands();
  }

  @Operation(summary = "Получить бренд по ID")
  @ApiResponse(responseCode = "200", description = "Бренд найден")
  @ApiResponse(responseCode = "404", description = "Бренд не найден")
  @GetMapping("/{id}")
  public BrandDto getBrandById(@PathVariable Long id) {
    return service.getBrandById(id);
  }

  @Operation(summary = "Создать бренд")
  @ApiResponse(responseCode = "200", description = "Бренд создан")
  @ApiResponse(responseCode = "400", description = "Ошибка валидации")
  @PostMapping
  public BrandDto createBrand(@RequestBody BrandDto dto) {
    return service.createBrand(dto);
  }

  @Operation(summary = "Обновить бренд")
  @ApiResponse(responseCode = "200", description = "Бренд обновлен")
  @ApiResponse(responseCode = "404", description = "Бренд не найден")
  @PutMapping("/{id}")
  public BrandDto updateBrand(@PathVariable Long id, @RequestBody BrandDto dto) {
    return service.updateBrand(id, dto);
  }

  @Operation(summary = "Удалить бренд")
  @ApiResponse(responseCode = "204", description = "Бренд удален")
  @ApiResponse(responseCode = "404", description = "Бренд не найден")
  @DeleteMapping("/{id}")
  public void deleteBrand(@PathVariable Long id) {
    service.deleteBrand(id);
  }
}