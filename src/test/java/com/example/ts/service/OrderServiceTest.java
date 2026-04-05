package com.example.ts.service;

import com.example.ts.domain.Customer;
import com.example.ts.domain.Order;
import com.example.ts.domain.Toy;
import com.example.ts.dto.OrderItemRequestDto;
import com.example.ts.dto.OrderRequestDto;
import com.example.ts.repository.CustomerRepository;
import com.example.ts.repository.OrderRepository;
import com.example.ts.repository.ToyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

  @Mock
  private OrderRepository orderRepository;

  @Mock
  private CustomerRepository customerRepository;

  @Mock
  private ToyRepository toyRepository;

  @InjectMocks
  private OrderService service;

  private Customer customer;
  private Toy toy;

  @BeforeEach
  void setUp() {
    customer = new Customer();
    customer.setId(1L);

    toy = new Toy();
    toy.setId(1L);
    toy.setQuantity(10);
    toy.setPrice(100.0);
  }

  // ===== helper =====
  private OrderRequestDto buildRequest(int qty) {
    return new OrderRequestDto(
        1L,
        List.of(new OrderItemRequestDto(1L, qty))
    );
  }

  // ===== tests =====

  @Test
  void createOrder_success() {
    when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
    when(toyRepository.findById(1L)).thenReturn(Optional.of(toy));
    when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

    OrderRequestDto request = buildRequest(2);

    Order result = service.createOrder(request);

    assertNotNull(result);
    assertEquals(1, result.getItems().size());
    verify(orderRepository).save(any(Order.class));
  }

  @Test
  void createOrder_customerNotFound() {
    when(customerRepository.findById(1L)).thenReturn(Optional.empty());

    OrderRequestDto request = buildRequest(1);

    assertThrows(ResponseStatusException.class,
        () -> service.createOrder(request));
  }

  @Test
  void createOrder_toyNotFound() {
    when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
    when(toyRepository.findById(1L)).thenReturn(Optional.empty());

    OrderRequestDto request = buildRequest(1);

    assertThrows(ResponseStatusException.class,
        () -> service.createOrder(request));
  }

  @Test
  void createOrder_notEnoughStock() {
    toy.setQuantity(1);

    when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
    when(toyRepository.findById(1L)).thenReturn(Optional.of(toy));

    OrderRequestDto request = buildRequest(5);

    assertThrows(ResponseStatusException.class,
        () -> service.createOrder(request));
  }

  @Test
  void getOrderById_notFound() {
    when(orderRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(ResponseStatusException.class,
        () -> service.getOrderById(1L));
  }

  @Test
  void deleteOrder_notFound() {
    when(orderRepository.existsById(1L)).thenReturn(false);

    assertThrows(ResponseStatusException.class,
        () -> service.deleteOrder(1L));
  }

  @Test
  void createOrderWithoutTransaction_throws() {
    when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
    when(toyRepository.findAll()).thenReturn(List.of(toy));

    OrderRequestDto request = buildRequest(1);

    assertThrows(IllegalStateException.class,
        () -> service.createOrderWithoutTransaction(request));
  }

  @Test
  void createOrderWithTransaction_throws() {
    when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
    when(toyRepository.findAll()).thenReturn(List.of(toy));

    OrderRequestDto request = buildRequest(1);

    assertThrows(IllegalStateException.class,
        () -> service.createOrderWithTransaction(request));
  }

  @Test
  void getAllOrders_success() {
    when(orderRepository.findAllBy()).thenReturn(List.of(new Order(), new Order()));

    List<Order> result = service.getAllOrders();

    assertEquals(2, result.size());
    verify(orderRepository).findAllBy();
  }

  @Test
  void getOrderById_success() {
    Order order = new Order();
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

    Order result = service.getOrderById(1L);

    assertNotNull(result);
    verify(orderRepository).findById(1L);
  }

  @Test
  void updateOrder_success() {
    Order order = new Order();
    order.setItems(new java.util.ArrayList<>());

    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
    when(toyRepository.findById(1L)).thenReturn(Optional.of(toy));
    when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

    OrderRequestDto request = buildRequest(2);

    Order result = service.updateOrder(1L, request);

    assertNotNull(result);
    assertEquals(1, result.getItems().size());
    verify(orderRepository).save(order);
  }

  @Test
  void deleteOrder_success() {
    when(orderRepository.existsById(1L)).thenReturn(true);

    service.deleteOrder(1L);

    verify(orderRepository).deleteById(1L);
  }

  @Test
  void updateOrder_notFound() {
    when(orderRepository.findById(1L)).thenReturn(Optional.empty());

    OrderRequestDto request = buildRequest(1);

    assertThrows(ResponseStatusException.class,
        () -> service.updateOrder(1L, request));
  }

  @Test
  void createOrderWithoutTransaction_callsInternal() {
    when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
    when(toyRepository.findAll()).thenReturn(List.of(toy));

    OrderRequestDto request = buildRequest(1);

    assertThrows(IllegalStateException.class,
        () -> service.createOrderWithoutTransaction(request));

    verify(orderRepository).save(any(Order.class));
  }

  @Test
  void createOrderWithTransaction_callsInternal() {
    when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
    when(toyRepository.findAll()).thenReturn(List.of(toy));

    OrderRequestDto request = buildRequest(1);

    assertThrows(IllegalStateException.class,
        () -> service.createOrderWithTransaction(request));

    verify(orderRepository).save(any(Order.class));
  }

}