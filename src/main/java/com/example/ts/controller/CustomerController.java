package com.example.ts.controller;

import com.example.ts.domain.Customer;
import com.example.ts.service.CustomerService;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Клиенты", description = "API для управления клиентами")
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

  private final CustomerService customerService;

  @Operation(summary = "Создать клиента")
  @ApiResponse(responseCode = "200", description = "Клиент создан")
  @ApiResponse(responseCode = "400", description = "Ошибка валидации")
  @PostMapping
  public Customer create(@RequestBody Customer customer) {
    return customerService.create(customer);
  }

  @Operation(summary = "Получить всех клиентов")
  @ApiResponse(responseCode = "200", description = "Список клиентов получен")
  @GetMapping
  public List<Customer> getAll() {
    return customerService.getAll();
  }

  @Operation(summary = "Получить клиента по ID")
  @ApiResponse(responseCode = "200", description = "Клиент найден")
  @ApiResponse(responseCode = "404", description = "Клиент не найден")
  @GetMapping("/{id}")
  public Customer getById(@PathVariable Long id) {
    return customerService.getById(id);
  }

  @Operation(summary = "Удалить клиента")
  @ApiResponse(responseCode = "204", description = "Клиент удален")
  @ApiResponse(responseCode = "404", description = "Клиент не найден")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
    customerService.deleteCustomer(id);
    return ResponseEntity.noContent().build();
  }
}