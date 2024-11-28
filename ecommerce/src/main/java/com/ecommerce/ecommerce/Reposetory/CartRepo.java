package com.ecommerce.ecommerce.Reposetory;

import com.ecommerce.ecommerce.Entity.Cart;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart, String> {
    Optional<Cart> findByUser_Id(String userId);


    @Modifying
    @Transactional
    @Query(value = "DELETE FROM cart_item WHERE id = :cartItemId", nativeQuery = true)
    void deleteCartItemById(@Param("cartItemId") String cartItemId);
}