package com.digitalmart.application.Cart;

import com.digitalmart.application.Product.UnitType;

import java.math.BigDecimal;

public record CartItemResponseDto(
        Long productId,
        String productName,
        String imageUrl,
        Integer quantity,
        String description,
        UnitType unitType,
        BigDecimal unitValue,
        BigDecimal priceAtAdd,
        BigDecimal currentPrice
) {
}
