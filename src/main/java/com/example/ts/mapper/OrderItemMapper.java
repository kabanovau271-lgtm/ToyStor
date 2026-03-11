package com.example.ts.mapper;

import com.example.ts.domain.OrderItem;
import com.example.ts.dto.OrderItemDto;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {

  public OrderItemDto toDto(OrderItem item) {
    OrderItemDto dto = new OrderItemDto();
    dto.setId(item.getId());
    dto.setToyId(item.getToy().getId());
    dto.setQuantity(item.getQuantity());
    return dto;
  }
}