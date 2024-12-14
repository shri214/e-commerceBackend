package com.ecommerce.ecommerce.Reposetory;

import com.ecommerce.ecommerce.Entity.Cart;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart, String> {
    Optional<Cart> findByUser_Id(String userId);

    Page<Cart> findByUser_Id(Pageable pageable, String userId);

    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.product.id = :productId")
    void deleteByProduct_Id(@Param("productId") String productId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM cart_item WHERE id = :cartItemId", nativeQuery = true)
    void deleteCartItemById(@Param("cartItemId") String cartItemId);
}
