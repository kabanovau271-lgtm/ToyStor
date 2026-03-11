package com.example.ts.mapper;

import com.example.ts.domain.Brand;
import com.example.ts.dto.BrandDto;
import org.springframework.stereotype.Component;

@Component
public class BrandMapper {

  public BrandDto toDto(Brand brand) {
    BrandDto dto = new BrandDto();

    dto.setId(brand.getId());
    dto.setName(brand.getName());

    return dto;
  }

  public Brand toEntity(BrandDto dto) {
    Brand brand = new Brand();

    brand.setName(dto.getName());

    return brand;
  }
}