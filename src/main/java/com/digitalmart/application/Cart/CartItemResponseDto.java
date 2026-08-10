package com.digitalmart.application.Cart;

import java.math.BigDecimal;

public record CartItemResponseDto(
        Long productId,
        String productName,
        String imageUrl,
        Integer quantity,
        String description,
        BigDecimal priceAtAdd,
        BigDecimal currentPrice
) {
}
