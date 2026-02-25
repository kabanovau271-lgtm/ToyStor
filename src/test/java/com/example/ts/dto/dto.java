package com.example.ts.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ToyDtoTest {

  @Test
  void shouldSetAndGetRequestDto() {
    ToyRequestDto dto = new ToyRequestDto();

    dto.setName("Car");
    dto.setBrand("Brand");
    dto.setPrice(100.0);
    dto.setQuantity(5);

    assertEquals("Car", dto.getName());
    assertEquals("Brand", dto.getBrand());
    assertEquals(100.0, dto.getPrice());
    assertEquals(5, dto.getQuantity());
  }

  @Test
  void shouldSetAndGetResponseDto() {
    ToyResponseDto dto = new ToyResponseDto();

    dto.setId(1L);
    dto.setName("Car");
    dto.setBrand("Brand");
    dto.setPrice(100.0);
    dto.setQuantity(5);

    assertEquals(1L, dto.getId());
    assertEquals("Car", dto.getName());
    assertEquals("Brand", dto.getBrand());
    assertEquals(100.0, dto.getPrice());
    assertEquals(5, dto.getQuantity());
  }
}