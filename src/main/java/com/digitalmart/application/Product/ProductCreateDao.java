package com.digitalmart.application.Product;

import java.math.BigDecimal;

public record ProductCreateDao(
        Long id,
        String category,
        String name,
        String sku,
        String description,
        Integer availableStock,
        String unitType,
        BigDecimal unitValue,
        BigDecimal price,
        String imageUrl
) {
}
