package com.example.ts.mapper;

import com.example.ts.domain.Toy;
import com.example.ts.dto.ToyResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ToyMapper {

  public ToyResponseDto toDto(Toy toy) {
    ToyResponseDto dto = new ToyResponseDto();
    dto.setId(toy.getId());
    dto.setName(toy.getName());
    dto.setPrice(toy.getPrice());
    dto.setQuantity(toy.getQuantity());
    dto.setBrand(toy.getBrand().getName());
    return dto;
  }
}