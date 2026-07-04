package com.digitalmart.application.Cart;

import com.digitalmart.application.Product.Product;
import jakarta.persistence.*;

@Entity
public class CartItem {

    @EmbeddedId
    private CartItemId id = new CartItemId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("cartId") // Maps cartId from the composite key
    @JoinColumn(name = "cart_id")
    private ShoppingCart classCart;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId") // Maps productId from the composite key
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity", nullable = false, check = {
            @CheckConstraint(
                    constraint = "quantity >= 0"
            )
    })
    private Integer quantity; // Extra column made possible by this design
}
