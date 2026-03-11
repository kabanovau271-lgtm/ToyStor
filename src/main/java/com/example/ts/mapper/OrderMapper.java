package com.example.ts.mapper;

import com.example.ts.domain.Order;
import com.example.ts.dto.OrderDto;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

  public OrderDto toDto(Order order) {
    OrderDto dto = new OrderDto();
    dto.setId(order.getId());
    dto.setCustomerId(order.getCustomer().getId());
    return dto;
  }
}