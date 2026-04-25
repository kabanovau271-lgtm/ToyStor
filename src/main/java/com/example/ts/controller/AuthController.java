package com.example.ts.controller;

import com.example.ts.domain.Customer;
import com.example.ts.repository.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final CustomerRepository customerRepository;

  public AuthController(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
    return customerRepository.findAll()
        .stream()
        .filter(c -> c.getEmail().equals(request.getEmail())
            && c.getPassword().equals(request.getPassword()))
        .findFirst()
        .map(c -> ResponseEntity.ok(
            new LoginResponse(c.getId(), c.getName(), c.getEmail(), c.getRole())))
        .orElse(ResponseEntity.status(401).build());
  }
}