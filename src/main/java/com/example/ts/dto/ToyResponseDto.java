package com.example.ts.dto;

import java.util.Set;
import lombok.Data;

@Data
public class ToyResponseDto {

  private Long id;
  private String name;
  private Double price;
  private Integer quantity;
  private String brand;

  private Set<String> categories;
}