package com.digitalmart.application.Product;

import java.math.BigDecimal;

public record ProductCreateDao(
        Long id,
        String category,
        String name,
        String sku,
        String description,
        Integer availableStock,
        BigDecimal price,
        String imageUrl
) {
}
