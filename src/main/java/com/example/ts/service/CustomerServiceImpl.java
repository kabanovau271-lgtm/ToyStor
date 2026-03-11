package com.example.ts.service;

import com.example.ts.domain.Customer;
import com.example.ts.repository.CustomerRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

  private final CustomerRepository customerRepository;

  @Override
  public Customer create(Customer customer) {
    return customerRepository.save(customer);
  }

  @Override
  public List<Customer> getAll() {
    return customerRepository.findAll();
  }

  @Override
  public Customer getById(Long id) {
    return customerRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Customer not found"));
  }
  @Override

  public void deleteCustomer(Long id) {
    customerRepository.deleteById(id);
  }

}