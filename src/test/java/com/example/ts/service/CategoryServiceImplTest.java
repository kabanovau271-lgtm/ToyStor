package com.example.ts.service;

import com.example.ts.domain.Category;
import com.example.ts.dto.CategoryDto;
import com.example.ts.mapper.CategoryMapper;
import com.example.ts.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {

  @Mock private CategoryRepository repository;
  @Mock private CategoryMapper mapper;

  @InjectMocks private CategoryServiceImpl service;

  private Category category;
  private CategoryDto dto;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    category = new Category();
    category.setId(1L);
    category.setName("Cars");

    dto = new CategoryDto();
    dto.setName("Cars");
  }

  // ================= GET ALL =================

  @Test
  void getAllCategories() {
    when(repository.findAll()).thenReturn(List.of(category));
    when(mapper.toDto(any())).thenReturn(dto);

    List<CategoryDto> result = service.getAllCategories();

    assertEquals(1, result.size());
    verify(repository).findAll();
  }

  // ================= GET BY ID =================

  @Test
  void getCategoryById_success() {
    when(repository.findById(1L)).thenReturn(Optional.of(category));
    when(mapper.toDto(category)).thenReturn(dto);

    CategoryDto result = service.getCategoryById(1L);

    assertNotNull(result);
  }

  @Test
  void getCategoryById_notFound() {
    when(repository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class,
        () -> service.getCategoryById(1L));
  }

  // ================= CREATE =================

  @Test
  void createCategory() {
    when(mapper.toEntity(dto)).thenReturn(category);
    when(repository.save(category)).thenReturn(category);
    when(mapper.toDto(category)).thenReturn(dto);

    CategoryDto result = service.createCategory(dto);

    assertNotNull(result);
    verify(repository).save(category);
  }

  // ================= UPDATE =================

  @Test
  void updateCategory_success() {
    when(repository.findById(1L)).thenReturn(Optional.of(category));
    when(repository.save(any())).thenReturn(category);
    when(mapper.toDto(category)).thenReturn(dto);

    CategoryDto result = service.updateCategory(1L, dto);

    assertNotNull(result);
    verify(repository).save(category);
  }

  @Test
  void updateCategory_notFound() {
    when(repository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class,
        () -> service.updateCategory(1L, dto));
  }

  // ================= DELETE =================

  @Test
  void deleteCategory() {
    service.deleteCategory(1L);

    verify(repository).deleteById(1L);
  }
}