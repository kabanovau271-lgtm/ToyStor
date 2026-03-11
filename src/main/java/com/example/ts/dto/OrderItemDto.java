package com.example.ts.dto;

import lombok.Data;

@Data
public class OrderItemDto {

  private Long id;
  private Long toyId;
  private Integer quantity;

}