package com.example.ts.mapper;

import com.example.ts.domain.Toy;
import com.example.ts.dto.ToyRequestDto;
import com.example.ts.dto.ToyResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ToyMapper {

  public Toy toEntity(ToyRequestDto dto) {
    Toy toy = new Toy();
    toy.setName(dto.getName());
    toy.setBrand(dto.getBrand());
    toy.setPrice(dto.getPrice());
    toy.setQuantity(dto.getQuantity());
    return toy;
  }

  public ToyResponseDto toDto(Toy toy) {
    ToyResponseDto dto = new ToyResponseDto();
    dto.setId(toy.getId());
    dto.setName(toy.getName());
    dto.setBrand(toy.getBrand());
    dto.setPrice(toy.getPrice());
    dto.setQuantity(toy.getQuantity());
    return dto;
  }
}