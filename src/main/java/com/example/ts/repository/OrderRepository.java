package com.example.ts.repository;

import com.example.ts.domain.Order;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

  @Override
  @EntityGraph(attributePaths = "items")
  List<Order> findAll();
}