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
WHERE c.name = :categoryName
AND t.price >= :minPrice
""")
  Page<Toy> findByCategoryAndPrice(
      @Param("categoryName") String categoryName,
      @Param("minPrice") Double minPrice,
      Pageable pageable
  );
  @Query(
      value = "SELECT DISTINCT t.* FROM toys t " +
          "JOIN toy_category tc ON t.id = tc.toy_id " +
          "JOIN categories c ON tc.category_id = c.id " +
          "WHERE c.name = :categoryName " +
          "AND t.price >= :minPrice",
      countQuery = "SELECT COUNT(DISTINCT t.id) FROM toys t " +
          "JOIN toy_category tc ON t.id = tc.toy_id " +
          "JOIN categories c ON tc.category_id = c.id " +
          "WHERE c.name = :categoryName " +
          "AND t.price >= :minPrice",
      nativeQuery = true
  )
  Page<Toy> findByCategoryAndPriceNative(
      @Param("categoryName") String categoryName,
      @Param("minPrice") Double minPrice,
      Pageable pageable
  );
}