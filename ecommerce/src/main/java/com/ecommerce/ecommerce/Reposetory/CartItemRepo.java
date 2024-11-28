package com.ecommerce.ecommerce.Reposetory;

import com.ecommerce.ecommerce.Entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepo extends JpaRepository<CartItem, String> {
}
