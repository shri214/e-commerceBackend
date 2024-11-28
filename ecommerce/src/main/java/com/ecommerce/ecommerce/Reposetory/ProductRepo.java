package com.ecommerce.ecommerce.Reposetory;

import com.ecommerce.ecommerce.Entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product, String> {
    Optional<Product> findByName(String name);
    Page<Product> findAll(Pageable pageable);

}
