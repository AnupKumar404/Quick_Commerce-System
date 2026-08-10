package com.digitalmart.application.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);

    @Query("SELECT p.id, p.name, p.description, p.price, p.imageUrl FROM Product p")
    List<ProductDto> getAllProducts();

    @Query("SELECT p.id, p.name, p.availableStock, p.price FROM Product p")
    List<AdminDao> getAllProductsForDashboard();
}
