package com.example.ts.repository;

import com.example.ts.domain.Brand;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {

  Optional<Brand> findByName(String name);

}