package com.digitalmart.application.Cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("SELECT ci FROM CartItem ci JOIN FETCH ci.product WHERE ci.cart.id= :cartId")
    Optional<List<CartItem>> findByCartId(@Param("cartId") Long cartId);

    @Query("SELECT ci FROM CartItem ci WHERE ci.product.id= :productId AND ci.cart.id= :cartId")
    Optional<CartItem> findByProductIdAndCartId(@Param("productId") Long productId, @Param("cartId") Long cartId);
}
