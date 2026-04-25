package com.example.ts.repository;

import com.example.ts.domain.Toy;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ToyRepository extends JpaRepository<Toy, Long> {

  List<Toy> findByName(String name);

  @Query("SELECT DISTINCT t FROM Toy t LEFT JOIN FETCH t.categories")
  List<Toy> findAllWithCategories();

  @Query("SELECT DISTINCT t FROM Toy t LEFT JOIN FETCH t.categories WHERE t.id = :id")
  Optional<Toy> findByIdWithCategories(Long id);

  @EntityGraph(attributePaths = {"categories", "brand"})
  @Query("""
  SELECT t FROM Toy t
  JOIN t.categories c
  WHERE (:category IS NULL OR c.name = :category)
  AND t.price >= :minPrice
  AND t.price <= :maxPrice
""")
  Page<Toy> findByCategoryAndPriceBetween(
      @Param("category") String category,
      @Param("minPrice") Double minPrice,
      @Param("maxPrice") Double maxPrice,
      Pageable pageable
  );

  @Query(
      value = "SELECT DISTINCT t.* FROM toys t " +
          "JOIN toy_category tc ON t.id = tc.toy_id " +
          "JOIN categories c ON tc.category_id = c.id " +
          "WHERE (:category IS NULL OR c.name = :category) " +
          "AND t.price >= :minPrice " +
          "AND t.price <= :maxPrice",
      countQuery = "SELECT COUNT(DISTINCT t.id) FROM toys t " +
          "JOIN toy_category tc ON t.id = tc.toy_id " +
          "JOIN categories c ON tc.category_id = c.id " +
          "WHERE (:category IS NULL OR c.name = :category) " +
          "AND t.price >= :minPrice " +
          "AND t.price <= :maxPrice",
      nativeQuery = true
  )
  Page<Toy> findByCategoryAndPriceBetweenNative(
      @Param("category") String category,
      @Param("minPrice") Double minPrice,
      @Param("maxPrice") Double maxPrice,
      Pageable pageable
  );
}