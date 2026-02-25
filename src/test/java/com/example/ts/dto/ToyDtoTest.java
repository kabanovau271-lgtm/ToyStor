package com.example.ts.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ToyDtoTest {

  @Test
  void requestDto_gettersAndSetters_shouldWork() {
    ToyRequestDto dto = new ToyRequestDto();

    dto.setName("Car");
    dto.setBrand("Brand");
    dto.setPrice(10.0);
    dto.setQuantity(2);

    assertEquals("Car", dto.getName());
    assertEquals("Brand", dto.getBrand());
    assertEquals(10.0, dto.getPrice());
    assertEquals(2, dto.getQuantity());
  }

  @Test
  void responseDto_gettersAndSetters_shouldWork() {
    ToyResponseDto dto = new ToyResponseDto();

    dto.setId(1L);
    dto.setName("Car");
    dto.setBrand("Brand");
    dto.setPrice(10.0);
    dto.setQuantity(2);

    assertEquals(1L, dto.getId());
    assertEquals("Car", dto.getName());
    assertEquals("Brand", dto.getBrand());
    assertEquals(10.0, dto.getPrice());
    assertEquals(2, dto.getQuantity());
  }
}