package com.ecommerce.ecommerce.Reposetory;

import com.ecommerce.ecommerce.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Order, String> {
}
