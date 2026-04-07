package com.example.ts.service;

import com.example.ts.domain.Customer;
import com.example.ts.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

  @Mock private CustomerRepository repository;

  @InjectMocks private CustomerServiceImpl service;

  private Customer customer;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    customer = new Customer();
    customer.setId(1L);
    customer.setName("John");
  }

  @Test
  void create() {
    when(repository.save(customer)).thenReturn(customer);

    Customer result = service.create(customer);

    assertNotNull(result);
    verify(repository).save(customer);
  }

  @Test
  void getAll() {
    when(repository.findAll()).thenReturn(List.of(customer));

    List<Customer> result = service.getAll();

    assertEquals(1, result.size());
    verify(repository).findAll();
  }

  @Test
  void getById_success() {
    when(repository.findById(1L)).thenReturn(Optional.of(customer));

    Customer result = service.getById(1L);

    assertNotNull(result);
  }

  @Test
  void getById_notFound() {
    when(repository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class,
        () -> service.getById(1L));
  }

  @Test
  void deleteCustomer() {
    service.deleteCustomer(1L);

    verify(repository).deleteById(1L);
  }
}