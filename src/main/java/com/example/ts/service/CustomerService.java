package com.example.ts.service;

import com.example.ts.domain.Customer;
import java.util.List;

public interface CustomerService {

  Customer create(Customer customer);

  List<Customer> getAll();

  Customer getById(Long id);

  void deleteCustomer(Long id);
}