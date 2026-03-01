package com.example.ts.dto;

public record OrderItemRequestDto(
    Long toyId,
    Integer quantity
) {}