package com.example.ts.dto;

import lombok.Data;

@Data
public class ToyRequestDto {

  private String name;
  private Double price;
  private Integer quantity;
  private Long brandId;
}