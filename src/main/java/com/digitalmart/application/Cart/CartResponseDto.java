package com.digitalmart.application.Cart;

import java.math.BigDecimal;
import java.util.List;

public record CartResponseDto(
        Long cartId,
        List<CartItemResponseDto> items,
        BigDecimal totalAmount
) {
}
