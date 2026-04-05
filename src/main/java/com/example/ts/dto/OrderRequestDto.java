package com.example.ts.dto;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record OrderRequestDto(

    @NotNull(message = "Customer id is required")
    Long customerId,

    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    List<OrderItemRequestDto> items

) {}