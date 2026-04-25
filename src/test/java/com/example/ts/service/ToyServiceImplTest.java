package com.example.ts.service;

import com.example.ts.domain.Brand;
import com.example.ts.domain.Category;
import com.example.ts.domain.Toy;
import com.example.ts.dto.ToyRequestDto;
import com.example.ts.dto.ToyResponseDto;
import com.example.ts.mapper.ToyMapper;
import com.example.ts.repository.BrandRepository;
import com.example.ts.repository.CategoryRepository;
import com.example.ts.repository.OrderItemRepository;
import com.example.ts.repository.ToyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ToyServiceImplTest {

  @Mock private ToyRepository repository;
  @Mock private ToyMapper mapper;
  @Mock private BrandRepository brandRepository;
  @Mock private CategoryRepository categoryRepository;
  @Mock private OrderItemRepository orderItemRepository;

  @InjectMocks
  private ToyServiceImpl service;

  private Brand brand;
  private Category category;
  private Toy toy;
  private ToyRequestDto requestDto;
  private ToyResponseDto responseDto;

  @BeforeEach
  void setUp() {
    brand = new Brand(1L, "Lego", null);
    category = new Category(1L, "Конструкторы", null);

    toy = new Toy();
    toy.setId(1L);
    toy.setName("Test Toy");
    toy.setPrice(100.0);
    toy.setQuantity(10);
    toy.setBrand(brand);
    toy.setCategories(Set.of(category));

    requestDto = new ToyRequestDto();
    requestDto.setName("Test Toy");
    requestDto.setPrice(100.0);
    requestDto.setQuantity(10);
    requestDto.setBrandId(1L);
    requestDto.setCategoryIds(Set.of(1L));

    responseDto = new ToyResponseDto();
    responseDto.setId(1L);
    responseDto.setName("Test Toy");
    responseDto.setPrice(100.0);
    responseDto.setQuantity(10);
    responseDto.setBrand("Lego");
    responseDto.setCategories(Set.of("Конструкторы"));
  }

  @Test
  void getAllToys_ShouldReturnList() {
    when(repository.findAllWithCategories()).thenReturn(List.of(toy));
    when(mapper.toDto(toy)).thenReturn(responseDto);

    List<ToyResponseDto> result = service.getAllToys();

    assertEquals(1, result.size());
    assertEquals("Test Toy", result.get(0).getName());
    verify(repository).findAllWithCategories();
  }

  @Test
  void getToyById_ShouldReturnToy() {
    when(repository.findByIdWithCategories(1L)).thenReturn(Optional.of(toy));
    when(mapper.toDto(toy)).thenReturn(responseDto);

    ToyResponseDto result = service.getToyById(1L);

    assertEquals("Test Toy", result.getName());
  }

  @Test
  void getToysByName_ShouldReturnList() {
    when(repository.findByName("Test")).thenReturn(List.of(toy));
    when(mapper.toDto(toy)).thenReturn(responseDto);

    List<ToyResponseDto> result = service.getToysByName("Test");

    assertEquals(1, result.size());
  }

  @Test
  void createToy_ShouldSaveAndReturn() {
    when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
    when(repository.save(any(Toy.class))).thenReturn(toy);
    when(mapper.toDto(toy)).thenReturn(responseDto);

    ToyResponseDto result = service.createToy(requestDto);

    assertEquals("Test Toy", result.getName());
    verify(repository).save(any(Toy.class));
  }

  @Test
  void updateToy_ShouldUpdateAndReturn() {
    when(repository.findById(1L)).thenReturn(Optional.of(toy));
    when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
    when(repository.save(any(Toy.class))).thenReturn(toy);
    when(mapper.toDto(toy)).thenReturn(responseDto);

    ToyResponseDto result = service.updateToy(1L, requestDto);

    assertEquals("Test Toy", result.getName());
  }

  @Test
  void deleteToy_ShouldDelete() {
    when(repository.findById(1L)).thenReturn(Optional.of(toy));
    when(orderItemRepository.existsByToy_Id(1L)).thenReturn(false);

    service.deleteToy(1L);

    verify(repository).delete(toy);
  }

  @Test
  void getByCategoryAndPrice_WithMaxPrice_ShouldReturnPage() {
    Page<Toy> page = new PageImpl<>(List.of(toy));
    when(repository.findByCategoryAndPriceBetween(
        eq("Конструкторы"), eq(50.0), eq(150.0), any(PageRequest.class)))
        .thenReturn(page);
    when(mapper.toDto(toy)).thenReturn(responseDto);

    Page<ToyResponseDto> result = service.getByCategoryAndPrice(
        "Конструкторы", 50.0, 150.0, 0, 10);

    assertEquals(1, result.getTotalElements());
    verify(repository).findByCategoryAndPriceBetween(
        eq("Конструкторы"), eq(50.0), eq(150.0), any(PageRequest.class));
  }

  @Test
  void getByCategoryAndPrice_WithNullMaxPrice_ShouldUseMaxValue() {
    Page<Toy> page = new PageImpl<>(List.of(toy));
    when(repository.findByCategoryAndPriceBetween(
        eq("Конструкторы"), eq(0.0), eq(Double.MAX_VALUE), any(PageRequest.class)))
        .thenReturn(page);
    when(mapper.toDto(toy)).thenReturn(responseDto);

    Page<ToyResponseDto> result = service.getByCategoryAndPrice(
        "Конструкторы", null, null, 0, 10);

    assertEquals(1, result.getTotalElements());
  }

  @Test
  void getByCategoryAndPriceNative_ShouldReturnPage() {
    Page<Toy> page = new PageImpl<>(List.of(toy));
    when(repository.findByCategoryAndPriceBetweenNative(
        eq("Конструкторы"), eq(50.0), eq(150.0), any(PageRequest.class)))
        .thenReturn(page);
    when(mapper.toDto(toy)).thenReturn(responseDto);

    Page<ToyResponseDto> result = service.getByCategoryAndPriceNative(
        "Конструкторы", 50.0, 150.0, 0, 10);

    assertEquals(1, result.getTotalElements());
  }

  @Test
  void getAllToysPaged_ShouldReturnPage() {
    Page<Toy> page = new PageImpl<>(List.of(toy));
    when(repository.findAll(any(PageRequest.class))).thenReturn(page);
    when(mapper.toDto(toy)).thenReturn(responseDto);

    Page<ToyResponseDto> result = service.getAllToysPaged(0, 10);

    assertEquals(1, result.getTotalElements());
  }
}