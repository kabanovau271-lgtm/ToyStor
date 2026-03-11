package com.example.ts.dto;

public record OrderItemResponseDto(
    Long toyId,
    String toyName,
    Integer quantity,
    Double price
) {}