package com.example.ts.service;

import com.example.ts.dto.BrandDto;
import java.util.List;

public interface BrandService {

  List<BrandDto> getAllBrands();

  BrandDto getBrandById(Long id);

  BrandDto createBrand(BrandDto dto);

  BrandDto updateBrand(Long id, BrandDto dto);

  void deleteBrand(Long id);
}