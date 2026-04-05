package com.example.ts.dto;

import java.util.Set;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ToyRequestDto {

  @NotBlank(message = "Name must not be empty")
  @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
  private String name;

  @NotNull(message = "Price is required")
  @DecimalMin(value = "0.1", message = "Price must be greater than 0")
  @DecimalMax(value = "10000", message = "Price is too large")
  private Double price;

  @NotNull(message = "Quantity is required")
  @Min(value = 0, message = "Quantity cannot be negative")
  private Integer quantity;

  @NotNull(message = "Brand is required")
  private Long brandId;

  @NotEmpty(message = "At least one category is required")
  private Set<Long> categoryIds;
}
