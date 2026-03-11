package com.example.ts.mapper;

import com.example.ts.domain.Customer;
import com.example.ts.dto.CustomerDto;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

  public CustomerDto toDto(Customer customer) {
    CustomerDto dto = new CustomerDto();
    dto.setId(customer.getId());
    dto.setName(customer.getName());
    dto.setEmail(customer.getEmail());
    return dto;
  }

  public Customer toEntity(CustomerDto dto) {
    Customer customer = new Customer();
    customer.setId(dto.getId());
    customer.setName(dto.getName());
    customer.setEmail(dto.getEmail());
    return customer;
  }
}