package com.example.ts.mapper;

import com.example.ts.domain.Toy;
import com.example.ts.dto.ToyRequestDto;
import com.example.ts.dto.ToyResponseDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ToyMapperTest {

  private final ToyMapper mapper = new ToyMapper();

  @Test
  void shouldMapToEntity() {
    ToyRequestDto dto = new ToyRequestDto();
    dto.setName("Car");
    dto.setBrand("Brand");
    dto.setPrice(100.0);
    dto.setQuantity(5);

    Toy toy = mapper.toEntity(dto);

    assertEquals("Car", toy.getName());
    assertEquals("Brand", toy.getBrand());
    assertEquals(100.0, toy.getPrice());
    assertEquals(5, toy.getQuantity());
  }

  @Test
  void shouldMapToDto() {
    Toy toy = new Toy();
    toy.setId(1L);
    toy.setName("Car");
    toy.setBrand("Brand");
    toy.setPrice(100.0);
    toy.setQuantity(5);

    ToyResponseDto dto = mapper.toDto(toy);

    assertEquals(1L, dto.getId());
    assertEquals("Car", dto.getName());
    assertEquals("Brand", dto.getBrand());
    assertEquals(100.0, dto.getPrice());
    assertEquals(5, dto.getQuantity());
  }
}