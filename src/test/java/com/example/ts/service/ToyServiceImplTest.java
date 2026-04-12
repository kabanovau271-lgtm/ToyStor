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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
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

  private Toy toy;
  private Brand brand;
  private Category category;
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

  @Test
  void getAllToys() {
    when(repository.findAllWithCategories()).thenReturn(List.of(toy));
    when(mapper.toDto(any())).thenReturn(new ToyResponseDto());

    List<ToyResponseDto> result = service.getAllToys();

    assertEquals(1, result.size());
    verify(repository).findAllWithCategories();
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

  @Test
  void updateToy_brandNotFound() {
    when(repository.findById(1L)).thenReturn(Optional.of(toy));
    when(brandRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(AppException.class, () -> service.updateToy(1L, dto));
  }

  @Test
  void updateToy_categoryNotFound() {
    when(repository.findById(1L)).thenReturn(Optional.of(toy));
    when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
    when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(AppException.class, () -> service.updateToy(1L, dto));
  }

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

    List<ToyRequestDto> list = List.of(dto, bad);

    assertThrows(AppException.class,
        () -> service.createToysBulkNoTx(list));
  }

  @Test
  void bulk_tx_error() {
    ToyRequestDto bad = new ToyRequestDto();
    bad.setName("ERROR");

    List<ToyRequestDto> list = List.of(dto, bad);

    assertThrows(AppException.class,
        () -> service.createToysBulkTx(list));
  }

  @Test
  void bulk_noTx_success() {
    when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
    when(repository.save(any())).thenReturn(toy);
    when(mapper.toDto(any())).thenReturn(new ToyResponseDto());

    List<ToyResponseDto> result =
        service.createToysBulkNoTx(List.of(dto, dto));

    assertEquals(2, result.size());
  }

  @Test
  void bulk_tx_success() {
    when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
    when(repository.save(any())).thenReturn(toy);
    when(mapper.toDto(any())).thenReturn(new ToyResponseDto());

    List<ToyResponseDto> result =
        service.createToysBulkTx(List.of(dto, dto));

    assertEquals(2, result.size());
  }

  @Test
  void getByCategoryAndPrice_cacheReturn() {
    Page<Toy> page = new PageImpl<>(List.of(toy));

    when(repository.findByCategoryAndPrice(any(), any(), any()))
        .thenReturn(page);
    when(mapper.toDto(any())).thenReturn(new ToyResponseDto());

    var first = service.getByCategoryAndPrice("cat", 10.0, 0, 10);
    var second = service.getByCategoryAndPrice("cat", 10.0, 0, 10);

    assertSame(first, second);
  }

  @Test
  void bulk_categoryNotFound() {
    when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
    when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

    List<ToyRequestDto> list = List.of(dto);
    assertThrows(AppException.class,
        () -> service.createToysBulk(list));
  }

  @Test
  void bulk_brandNotFound() {
    when(brandRepository.findById(1L)).thenReturn(Optional.empty());

    List<ToyRequestDto> list = List.of(dto);

    assertThrows(AppException.class,
        () -> service.createToysBulk(list));
  }

  @Test
  void getByCategoryAndPrice_cacheHit_returnFromCache() {
    Page<Toy> page = new PageImpl<>(List.of(toy));
    ToyResponseDto dtoResp = new ToyResponseDto();

    when(repository.findByCategoryAndPrice(any(), any(), any()))
        .thenReturn(page);
    when(mapper.toDto(any())).thenReturn(dtoResp);

    service.getByCategoryAndPrice("cat", 10.0, 0, 10);

    Page<ToyResponseDto> result =
        service.getByCategoryAndPrice("cat", 10.0, 0, 10);

    assertEquals(1, result.getContent().size());
    verify(repository, times(1))
        .findByCategoryAndPrice(any(), any(), any());
  }

  @Test
  void createToy_clearsCache() {
    Page<Toy> page = new PageImpl<>(List.of(toy));
    ToyResponseDto dtoResp = new ToyResponseDto();

    when(repository.findByCategoryAndPrice(any(), any(), any()))
        .thenReturn(page, page);
    when(mapper.toDto(any())).thenReturn(dtoResp);

    when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
    when(repository.save(any())).thenReturn(toy);

    service.getByCategoryAndPrice("cat", 10.0, 0, 10);

    service.createToy(dto);

    service.getByCategoryAndPrice("cat", 10.0, 0, 10);

    verify(repository, times(2))
        .findByCategoryAndPrice(any(), any(), any());
  }

  @Test
  void bulk_mixed_errorInsideStream() {
    ToyRequestDto bad = new ToyRequestDto();
    bad.setName("OK");
    bad.setBrandId(1L);
    bad.setCategoryIds(Set.of(1L));

    when(brandRepository.findById(1L))
        .thenReturn(Optional.of(brand));
    when(categoryRepository.findById(1L))
        .thenReturn(Optional.empty());

    List<ToyRequestDto> list = List.of(dto, bad);

    assertThrows(AppException.class,
        () -> service.createToysBulk(list));
  }

  @Test
  void cache_hit_returnsSameObject() {
    Page<Toy> page = new PageImpl<>(List.of(toy));
    ToyResponseDto dtoResp = new ToyResponseDto();

    when(repository.findByCategoryAndPrice(any(), any(), any()))
        .thenReturn(page);
    when(mapper.toDto(any())).thenReturn(dtoResp);

    Page<ToyResponseDto> first =
        service.getByCategoryAndPrice("cat", 10.0, 0, 10);

    Page<ToyResponseDto> second =
        service.getByCategoryAndPrice("cat", 10.0, 0, 10);

    assertSame(first, second);
  }

  @Test
  void toyNotFound_privateMethodCovered() {
    when(repository.findById(999L)).thenReturn(Optional.empty());

    assertThrows(AppException.class,
        () -> service.updateToy(999L, dto));
  }

  @Test
  void bulk_noTx_error_firstElement() {
    ToyRequestDto bad = new ToyRequestDto();
    bad.setName("ERROR");

    List<ToyRequestDto> list = List.of(bad, dto);

    assertThrows(AppException.class,
        () -> service.createToysBulkNoTx(list));
  }

  @Test
  void bulk_tx_error_firstElement() {
    ToyRequestDto bad = new ToyRequestDto();
    bad.setName("ERROR");

    List<ToyRequestDto> list = List.of(bad, dto);

    assertThrows(AppException.class,
        () -> service.createToysBulkTx(list));
  }

  @Test
  void bulk_noTx_fullBranchCoverage() {
    ToyRequestDto ok = dto;

    ToyRequestDto bad = new ToyRequestDto();
    bad.setName("ERROR");

    when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
    when(repository.save(any())).thenReturn(toy);
    when(mapper.toDto(any())).thenReturn(new ToyResponseDto());

    List<ToyRequestDto> list = List.of(ok, bad);

    assertThrows(AppException.class,
        () -> service.createToysBulkNoTx(list));
  }

  @Test
  void bulk_tx_fullBranchCoverage() {
    ToyRequestDto ok = dto;

    ToyRequestDto bad = new ToyRequestDto();
    bad.setName("ERROR");

    when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
    when(repository.save(any())).thenReturn(toy);
    when(mapper.toDto(any())).thenReturn(new ToyResponseDto());

    List<ToyRequestDto> list = List.of(ok, bad);

    assertThrows(AppException.class,
        () -> service.createToysBulkTx(list));
  }

  @Test
  void getAllToysPaged_success() {
    Page<Toy> page = new PageImpl<>(List.of(toy));

    when(repository.findAll(any(org.springframework.data.domain.Pageable.class)))
        .thenReturn(page);
    when(mapper.toDto(any())).thenReturn(new ToyResponseDto());

    Page<ToyResponseDto> result = service.getAllToysPaged(0, 10);

    assertNotNull(result);
    assertEquals(1, result.getContent().size());

    verify(repository).findAll(any(org.springframework.data.domain.Pageable.class));  }

}