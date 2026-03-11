package com.example.ts.repository;

import com.example.ts.domain.Order;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

  @EntityGraph(attributePaths = {
      "items",
      "items.toy",
      "items.toy.brand",
      "items.toy.categories"
  })
  List<Order> findAllBy();

}