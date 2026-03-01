package com.example.ts.controller;

import com.example.ts.domain.Order;
import com.example.ts.dto.OrderRequestDto;
import com.example.ts.service.OrderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  public ResponseEntity<Order> create(@RequestBody OrderRequestDto request) {
    Order order = orderService.createOrder(request);
    return ResponseEntity.ok(order);
  }

  @GetMapping
  public ResponseEntity<List<Order>> getAll() {
    return ResponseEntity.ok(orderService.getAllOrders());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Order> getById(@PathVariable Long id) {
    return ResponseEntity.ok(orderService.getOrderById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Order> update(@PathVariable Long id,
                                      @RequestBody OrderRequestDto request) {
    return ResponseEntity.ok(orderService.updateOrder(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    orderService.deleteOrder(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/nplusone")
  public ResponseEntity<List<Order>> testNPlusOne() {
    return ResponseEntity.ok(orderService.getAllOrdersWithItemsAccess());
  }

  @PostMapping("/demo/no-transaction/{customerId}")
  public ResponseEntity<Void> demoNoTransaction(@PathVariable Long customerId) {
    orderService.createOrderWithoutTransactionDemo(customerId);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/demo/with-transaction/{customerId}")
  public ResponseEntity<Void> demoWithTransaction(@PathVariable Long customerId) {
    orderService.createOrderWithTransactionDemo(customerId);
    return ResponseEntity.ok().build();
  }
}