package com.example.ts.service;

import com.example.ts.domain.Brand;
import com.example.ts.dto.BrandDto;
import com.example.ts.mapper.BrandMapper;
import com.example.ts.repository.BrandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BrandServiceImplTest {

  @Mock private BrandRepository repository;
  @Mock private BrandMapper mapper;

  @InjectMocks private BrandServiceImpl service;

  private Brand brand;
  private BrandDto dto;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    brand = new Brand();
    brand.setId(1L);
    brand.setName("Lego");

    dto = new BrandDto();
    dto.setName("Lego");
  }

  @Test
  void getAllBrands() {
    when(repository.findAll()).thenReturn(List.of(brand));
    when(mapper.toDto(any())).thenReturn(dto);

    List<BrandDto> result = service.getAllBrands();

    assertEquals(1, result.size());
    verify(repository).findAll();
  }

  @Test
  void getBrandById_success() {
    when(repository.findById(1L)).thenReturn(Optional.of(brand));
    when(mapper.toDto(brand)).thenReturn(dto);

    BrandDto result = service.getBrandById(1L);

    assertNotNull(result);
  }

  @Test
  void getBrandById_notFound() {
    when(repository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class,
        () -> service.getBrandById(1L));
  }

  @Test
  void createBrand() {
    when(mapper.toEntity(dto)).thenReturn(brand);
    when(repository.save(brand)).thenReturn(brand);
    when(mapper.toDto(brand)).thenReturn(dto);

    BrandDto result = service.createBrand(dto);

    assertNotNull(result);
    verify(repository).save(brand);
  }

  @Test
  void updateBrand_success() {
    when(repository.findById(1L)).thenReturn(Optional.of(brand));
    when(repository.save(any())).thenReturn(brand);
    when(mapper.toDto(brand)).thenReturn(dto);

    BrandDto result = service.updateBrand(1L, dto);

    assertNotNull(result);
    verify(repository).save(brand);
  }

  @Test
  void updateBrand_notFound() {
    when(repository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class,
        () -> service.updateBrand(1L, dto));
  }

  @Test
  void deleteBrand_success() {
    when(repository.existsById(1L)).thenReturn(true);

    service.deleteBrand(1L);

    verify(repository).deleteById(1L);
  }

  @Test
  void deleteBrand_notFound() {
    when(repository.existsById(1L)).thenReturn(false);

    assertThrows(IllegalArgumentException.class,
        () -> service.deleteBrand(1L));
  }
}