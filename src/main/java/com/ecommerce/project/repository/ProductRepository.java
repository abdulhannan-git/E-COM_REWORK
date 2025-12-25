package com.ecommerce.project.repository;

import com.ecommerce.project.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByActiveTrue();

    @Query("select p from Product_table p where p.active=true and LOWER(p.name) LIKE LOWER(CONCAT('%',:keyword,'%')) and p.stockQuantity>0")
    List<Product> searchProducts(String keyword);
}
