package com.example.ts.dto;

import java.util.List;

public record OrderRequestDto(
    Long customerId,
    List<OrderItemRequestDto> items
) {}