package com.example.ts.controller;

import com.example.ts.dto.ToyRequestDto;
import com.example.ts.dto.ToyResponseDto;
import com.example.ts.service.ToyService;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Игрушки", description = "API для управления игрушками")
@RestController
@RequestMapping("/toys")
public class ToyController {

  private final ToyService service;

  public ToyController(ToyService service) {
    this.service = service;
  }

  @Operation(summary = "Получить все игрушки")
  @GetMapping
  public List<ToyResponseDto> getAll() {
    return service.getAllToys();
  }

  @Operation(summary = "Получить игрушку по ID")
  @GetMapping("/{id}")
  public ToyResponseDto getById(
      @Parameter(description = "ID игрушки") @PathVariable Long id) {
    return service.getToyById(id);
  }

  @Operation(summary = "Поиск игрушек по названию")
  @GetMapping("/search")
  public List<ToyResponseDto> getByName(
      @Parameter(description = "Название игрушки") @RequestParam String name) {
    return service.getToysByName(name);
  }

  @Operation(summary = "Создать новую игрушку")
  @PostMapping
  public ToyResponseDto create(@Valid @RequestBody ToyRequestDto dto) {
    return service.createToy(dto);
  }

  @Operation(summary = "Обновить игрушку")
  @PutMapping("/{id}")
  public ToyResponseDto update(
      @Parameter(description = "ID игрушки") @PathVariable Long id,
      @Valid @RequestBody ToyRequestDto dto) {
    return service.updateToy(id, dto);
  }

  @Operation(summary = "Удалить игрушку")
  @DeleteMapping("/{id}")
  public void delete(
      @Parameter(description = "ID игрушки") @PathVariable Long id) {
    service.deleteToy(id);
  }

  @Operation(summary = "Фильтр по категории и цене (JPQL)")
  @GetMapping("/filter")
  public Page<ToyResponseDto> filter(
      @Parameter(description = "Категория") @RequestParam String category,
      @Parameter(description = "Минимальная цена") @RequestParam Double minPrice,
      @Parameter(description = "Номер страницы (>=0)") @RequestParam @Min(0) int page,
      @Parameter(description = "Размер страницы (1-50)") @RequestParam @Min(1) @Max(50) int size
  ) {
    return service.getByCategoryAndPrice(category, minPrice, page, size);
  }

  @Operation(summary = "Фильтр по категории и цене (Native SQL)")
  @GetMapping("/filter-native")
  public Page<ToyResponseDto> filterNative(
      @Parameter(description = "Категория") @RequestParam String category,
      @Parameter(description = "Минимальная цена") @RequestParam Double minPrice,
      @Parameter(description = "Номер страницы (>=0)") @RequestParam @Min(0) int page,
      @Parameter(description = "Размер страницы (1-50)") @RequestParam @Min(1) @Max(50) int size
  ) {
    return service.getByCategoryAndPriceNative(category, minPrice, page, size);
  }
}