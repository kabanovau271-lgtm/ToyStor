package com.example.ts.service;

import com.example.ts.domain.Brand;
import com.example.ts.dto.BrandDto;
import com.example.ts.mapper.BrandMapper;
import com.example.ts.repository.BrandRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BrandServiceImpl implements BrandService {

  private final BrandRepository repository;
  private final BrandMapper mapper;

  public BrandServiceImpl(BrandRepository repository, BrandMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public List<BrandDto> getAllBrands() {
    return repository.findAll()
        .stream()
        .map(mapper::toDto)
        .toList();
  }

  @Override
  public BrandDto getBrandById(Long id) {
    Brand brand = repository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Brand not found"));

    return mapper.toDto(brand);
  }

  @Override
  public BrandDto createBrand(BrandDto dto) {
    Brand brand = mapper.toEntity(dto);
    Brand saved = repository.save(brand);

    return mapper.toDto(saved);
  }

  @Override
  public BrandDto updateBrand(Long id, BrandDto dto) {
    Brand brand = repository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Brand not found"));

    brand.setName(dto.getName());

    Brand updated = repository.save(brand);
    return mapper.toDto(updated);
  }

  @Override
  public void deleteBrand(Long id) {
    if (!repository.existsById(id)) {
      throw new IllegalArgumentException("Brand not found");
    }
    repository.deleteById(id);
  }
}