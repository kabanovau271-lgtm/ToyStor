package com.example.ts.repository;

import com.example.ts.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

  void deleteByToy_Id(Long toyId);
  boolean existsByToy_Id(Long toyId);

}