package com.example.ts.repository;

import com.example.ts.domain.Toy;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToyRepository extends JpaRepository<Toy, Long> {
  List<Toy> findByName(String name);
}
