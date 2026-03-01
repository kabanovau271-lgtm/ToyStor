package com.example.ts.controller;

import com.example.ts.domain.Customer;
import com.example.ts.service.CustomerService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

  private final CustomerService customerService;

  @PostMapping
  public Customer create(@RequestBody Customer customer) {
    return customerService.create(customer);
  }

  @GetMapping
  public List<Customer> getAll() {
    return customerService.getAll();
  }

  @GetMapping("/{id}")
  public Customer getById(@PathVariable Long id) {
    return customerService.getById(id);
  }
}