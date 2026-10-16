package com.digitalmart.application.Product;

import java.math.BigDecimal;

public record ProductDto(
        Long id,
        String name,
        String description,
        BigDecimal price,
        UnitType unitType,
        BigDecimal unitValue,
        String imageUrl
) {
}
