package com.digitalmart.application.Product;

import java.math.BigDecimal;

public record AdminDao(
        Long id,
        String name,
        Integer availableStock,
        BigDecimal price
) {
}
