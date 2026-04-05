package com.example.ts.service;

import com.example.ts.domain.Brand;
import com.example.ts.domain.Category;
import com.example.ts.domain.Toy;
import com.example.ts.dto.ToyRequestDto;
import com.example.ts.dto.ToyResponseDto;
import com.example.ts.exception.AppException;
import com.example.ts.mapper.ToyMapper;
import com.example.ts.repository.BrandRepository;
import com.example.ts.repository.CategoryRepository;
import com.example.ts.repository.OrderItemRepository;
import com.example.ts.repository.ToyRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ToyServiceImplTest {

  @Mock private ToyRepository repository;
  @Mock private ToyMapper mapper;
  @Mock private BrandRepository brandRepository;
  @Mock private CategoryRepository categoryRepository;
  @Mock private OrderItemRepository orderItemRepository;

  @InjectMocks private ToyServiceImpl service;

  private Brand brand;
  private Category category;
  private Toy toy;
  private ToyRequestDto dto;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    brand = new Brand();
    brand.setId(1L);

    category = new Category();
    category.setId(1L);

    toy = new Toy();
    toy.setId(1L);
    toy.setName("Lego");

    dto = new ToyRequestDto();
    dto.setName("Lego");
    dto.setPrice(100.0);
    dto.setQuantity(5);
    dto.setBrandId(1L);
    dto.setCategoryIds(Set.of(1L));
  }

  // ================= READ =================

  @Test
  void getAllToys() {
    when(repository.findAllWithCategories()).thenReturn(List.of(toy));
    when(mapper.toDto(any())).thenReturn(new ToyResponseDto());

    assertEquals(1, service.getAllToys().size());
  }

  @Test
  void getToyById_success() {
    when(repository.findByIdWithCategories(1L)).thenReturn(Optional.of(toy));
    when(mapper.toDto(any())).thenReturn(new ToyResponseDto());

    assertNotNull(service.getToyById(1L));
  }

  @Test
  void getToyById_notFound() {
    when(repository.findByIdWithCategories(1L)).thenReturn(Optional.empty());
    assertThrows(AppException.class, () -> service.getToyById(1L));
  }

  @Test
  void getToysByName() {
    when(repository.findByName("Lego")).thenReturn(List.of(toy));
    when(mapper.toDto(any())).thenReturn(new ToyResponseDto());

    assertEquals(1, service.getToysByName("Lego").size());
  }

  // ================= CREATE =================

  @Test
  void createToy_success() {
    when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
    when(repository.save(any())).thenReturn(toy);
    when(mapper.toDto(any())).thenReturn(new ToyResponseDto());

    assertNotNull(service.createToy(dto));
  }

  @Test
  void createToy_brandNotFound() {
    when(brandRepository.findById(1L)).thenReturn(Optional.empty());
    assertThrows(AppException.class, () -> service.createToy(dto));
  }

  @Test
  void createToy_categoryNotFound() {
    when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
    when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
    assertThrows(AppException.class, () -> service.createToy(dto));
  }

  // ================= UPDATE =================

  @Test
  void updateToy_success() {
    when(repository.findById(1L)).thenReturn(Optional.of(toy));
    when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
    when(repository.save(any())).thenReturn(toy);
    when(mapper.toDto(any())).thenReturn(new ToyResponseDto());

    assertNotNull(service.updateToy(1L, dto));
  }

  @Test
  void updateToy_notFound() {
    when(repository.findById(1L)).thenReturn(Optional.empty());
    assertThrows(AppException.class, () -> service.updateToy(1L, dto));
  }

  // ================= DELETE =================

  @Test
  void deleteToy_success() {
    when(repository.findById(1L)).thenReturn(Optional.of(toy));
    when(orderItemRepository.existsByToy_Id(1L)).thenReturn(false);

    service.deleteToy(1L);

    verify(repository).delete(toy);
  }

  @Test
  void deleteToy_usedInOrder() {
    when(repository.findById(1L)).thenReturn(Optional.of(toy));
    when(orderItemRepository.existsByToy_Id(1L)).thenReturn(true);

    assertThrows(AppException.class, () -> service.deleteToy(1L));
  }

  @Test
  void deleteToy_notFound() {
    when(repository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(AppException.class, () -> service.deleteToy(1L));
  }

  // ================= CACHE =================

  @Test
  void getByCategoryAndPrice_cacheMissAndHit() {
    Page<Toy> page = new PageImpl<>(List.of(toy));

    when(repository.findByCategoryAndPrice(any(), any(), any()))
        .thenReturn(page);
    when(mapper.toDto(any())).thenReturn(new ToyResponseDto());

    service.getByCategoryAndPrice("cat", 10.0, 0, 10);
    service.getByCategoryAndPrice("cat", 10.0, 0, 10);

    verify(repository, times(1))
        .findByCategoryAndPrice(any(), any(), any());
  }

  @Test
  void getByCategoryAndPriceNative() {
    Page<Toy> page = new PageImpl<>(List.of(toy));

    when(repository.findByCategoryAndPriceNative(any(), any(), any()))
        .thenReturn(page);
    when(mapper.toDto(any())).thenReturn(new ToyResponseDto());

    assertEquals(1,
        service.getByCategoryAndPriceNative("cat", 10.0, 0, 10)
            .getContent().size());
  }

  // ================= BULK =================

  @Test
  void bulk_success() {
    when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
    when(repository.save(any())).thenReturn(toy);
    when(mapper.toDto(any())).thenReturn(new ToyResponseDto());

    assertEquals(2,
        service.createToysBulk(List.of(dto, dto)).size());
  }

  @Test
  void bulk_noTx_error() {
    ToyRequestDto bad = new ToyRequestDto();
    bad.setName("ERROR");

    Executable executable = () -> service.createToysBulkNoTx(List.of(dto, bad));

    assertThrows(AppException.class, executable);
  }

  @Test
  void bulk_tx_error() {
    ToyRequestDto bad = new ToyRequestDto();
    bad.setName("ERROR");

    Executable executable = () -> service.createToysBulkTx(List.of(dto, bad));

    assertThrows(AppException.class, executable);
  }
}