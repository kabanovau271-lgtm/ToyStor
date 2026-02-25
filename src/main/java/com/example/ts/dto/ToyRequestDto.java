package com.example.ts.dto;

import lombok.Data;

@Data
public class ToyRequestDto {

  private String name;
  private String brand;
  private Double price;
  private Integer quantity;
}
