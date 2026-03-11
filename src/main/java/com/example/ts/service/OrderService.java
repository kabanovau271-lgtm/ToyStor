package com.example.ts.service;

import com.example.ts.domain.Customer;
import com.example.ts.domain.Order;
import com.example.ts.domain.OrderItem;
import com.example.ts.domain.Toy;
import com.example.ts.dto.OrderRequestDto;
import com.example.ts.repository.CustomerRepository;
import com.example.ts.repository.OrderRepository;
import com.example.ts.repository.ToyRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final CustomerRepository customerRepository;
  private final ToyRepository toyRepository;

  // ========================= CONSTANTS =========================

  private static final String CUSTOMER_NOT_FOUND = "Customer not found";
  private static final String TOY_NOT_FOUND = "Toy not found";
  private static final String ORDER_NOT_FOUND = "Order not found";
  private static final String NOT_ENOUGH_STOCK = "Not enough toys in stock";

  // ========================= CREATE =========================

  @Transactional
  public Order createOrder(OrderRequestDto request) {
    return buildAndSaveOrder(request);
  }

  // ========================= READ =========================

  public List<Order> getAllOrders() {
    return orderRepository.findAllBy();
  }

  public Order getOrderById(Long id) {
    return orderRepository.findById(id)
        .orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, ORDER_NOT_FOUND));
  }

  // ========================= UPDATE =========================

  @Transactional
  public Order updateOrder(Long id, OrderRequestDto request) {

    Order order = orderRepository.findById(id)
        .orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, ORDER_NOT_FOUND));

    Customer customer = findCustomer(request.customerId());
    order.setCustomer(customer);

    order.getItems().clear();

    List<OrderItem> items = buildOrderItems(request, order);
    order.setItems(items);

    return orderRepository.save(order);
  }

  // ========================= DELETE =========================

  public void deleteOrder(Long id) {

    if (!orderRepository.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, ORDER_NOT_FOUND);
    }

    orderRepository.deleteById(id);
  }

  // ========================= TRANSACTION DEMONSTRATION =========================

  public Order createOrderWithoutTransaction(OrderRequestDto request) {
    return createOrderInternal(request);
  }

  @Transactional
  public Order createOrderWithTransaction(OrderRequestDto request) {
    return createOrderInternal(request);
  }

  private Order createOrderInternal(OrderRequestDto request) {

    Customer customer = findCustomer(request.customerId());

    Order order = new Order();
    order.setCreatedAt(LocalDateTime.now());
    order.setCustomer(customer);

    orderRepository.save(order);

    Toy toy = toyRepository.findAll().get(0);
    toy.setQuantity(toy.getQuantity() - 1);

    throw new IllegalStateException("Transaction rollback demo");
  }
  // ========================= PRIVATE LOGIC =========================

  private Order buildAndSaveOrder(OrderRequestDto request) {

    Customer customer = findCustomer(request.customerId());

    Order order = new Order();
    order.setCreatedAt(LocalDateTime.now());
    order.setCustomer(customer);

    List<OrderItem> items = buildOrderItems(request, order);
    order.setItems(items);

    return orderRepository.save(order);
  }

  private List<OrderItem> buildOrderItems(OrderRequestDto request, Order order) {

    return request.items().stream().map(itemDto -> {

      Toy toy = toyRepository.findById(itemDto.toyId())
          .orElseThrow(() ->
              new ResponseStatusException(HttpStatus.NOT_FOUND, TOY_NOT_FOUND));

      if (toy.getQuantity() < itemDto.quantity()) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            NOT_ENOUGH_STOCK
        );
      }

      toy.setQuantity(toy.getQuantity() - itemDto.quantity());

      OrderItem orderItem = new OrderItem();
      orderItem.setToy(toy);
      orderItem.setQuantity(itemDto.quantity());
      orderItem.setPriceAtPurchase(toy.getPrice());
      orderItem.setOrder(order);

      return orderItem;

    }).toList();
  }

  private Customer findCustomer(Long customerId) {

    return customerRepository.findById(customerId)
        .orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, CUSTOMER_NOT_FOUND));
  }
}