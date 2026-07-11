package com.digitalmart.application.Product;

import java.math.BigDecimal;

public record ProductCreateDao(
        Long id,
        String categoryName,
        String name,
        String sku,
        String description,
        BigDecimal price,
        String imageUrl
) {
}
