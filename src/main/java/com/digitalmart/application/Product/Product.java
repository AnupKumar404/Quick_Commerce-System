package com.digitalmart.application.Product;

import com.digitalmart.application.Category.Category;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "product_sku", unique = true)
    private String sku;

    @Column(name = "product_name", nullable = false)
    private String name;

    @Column(name = "product_description", nullable = false)
    private String description;

    @Column(name = "product_price", nullable = false, precision = 10, scale = 2,
            check = {
                @CheckConstraint(
                        constraint = "price > 0.00"
                )
            }
    )
    private BigDecimal price;

    @Column(name = "product_image")
    private String imageUrl;

    @Column(name = "available_stock", check = @CheckConstraint(
            constraint = "available_stock >= 0"
    ))
    private Integer availableStock;

    @Column(name = "unit_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private UnitType unitType;

    @Column(name = "unit_value", nullable = false)
    private BigDecimal unitValue;

    @Column(name = "is_available", columnDefinition = "boolean default true")
    private Boolean isAvailable = true;
}
