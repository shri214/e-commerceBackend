package com.ecommerce.ecommerce.Reposetory;

import com.ecommerce.ecommerce.Entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepo extends JpaRepository<OrderItem, String> {
}
