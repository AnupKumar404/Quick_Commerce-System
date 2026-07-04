package com.digitalmart.application.Product;

import java.math.BigDecimal;

public record ProductCreateDao(
        String categoryName,
        String name,
        String sku,
        String description,
        BigDecimal price,
        String imageUrl
) {
}
