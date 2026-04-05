package com.example.ts.controller;

import com.example.ts.domain.Order;
import com.example.ts.dto.OrderRequestDto;
import com.example.ts.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Заказы", description = "API для управления заказами")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @Operation(summary = "Создать заказ")
  @PostMapping
  public ResponseEntity<Order> create(
      @Valid @RequestBody OrderRequestDto request) {

    Order order = orderService.createOrder(request);
    return ResponseEntity.ok(order);
  }

  @Operation(summary = "Получить все заказы")
  @GetMapping
  public ResponseEntity<List<Order>> getAll() {
    return ResponseEntity.ok(orderService.getAllOrders());
  }

  @Operation(summary = "Получить заказ по ID")
  @GetMapping("/{id}")
  public ResponseEntity<Order> getById(
      @Parameter(description = "ID заказа") @PathVariable Long id) {

    return ResponseEntity.ok(orderService.getOrderById(id));
  }

  @Operation(summary = "Обновить заказ")
  @PutMapping("/{id}")
  public ResponseEntity<Order> update(
      @Parameter(description = "ID заказа") @PathVariable Long id,
      @Valid @RequestBody OrderRequestDto request) {

    return ResponseEntity.ok(orderService.updateOrder(id, request));
  }

  @Operation(summary = "Удалить заказ")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @Parameter(description = "ID заказа") @PathVariable Long id) {

    orderService.deleteOrder(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Создать заказ (с транзакцией)")
  @PostMapping("/with-transaction")
  public ResponseEntity<Order> createWithTransaction(
      @Valid @RequestBody OrderRequestDto request) {

    return ResponseEntity.ok(orderService.createOrderWithTransaction(request));
  }

  @Operation(summary = "Создать заказ (без транзакции)")
  @PostMapping("/without-transaction")
  public ResponseEntity<Order> createWithoutTransaction(
      @Valid @RequestBody OrderRequestDto request) {

    return ResponseEntity.ok(orderService.createOrderWithoutTransaction(request));
  }
}