package com.example.ts.service;

import com.example.ts.domain.Customer;
import com.example.ts.repository.CustomerRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class CustomerService {

  private final CustomerRepository customerRepository;

  public Customer create(Customer customer) {
    return customerRepository.save(customer);
  }

  public List<Customer> getAll() {
    return customerRepository.findAll();
  }

  public Customer getById(Long id) {
    return customerRepository.findById(id)
        .orElseThrow();
  }
}