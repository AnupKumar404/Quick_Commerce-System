package com.digitalmart.application.Cart;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CartItemId implements Serializable {
    private Long cartId;
    private Long productId;

    public CartItemId() {}

    public CartItemId(Long cartId, Long id) {
        this.cartId = cartId;
        this.productId = id;
    }

    // Must override equals and hashCode for composite keys
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItemId that)) return false;
        return Objects.equals(cartId, that.cartId) && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartId, productId);
    }
}

