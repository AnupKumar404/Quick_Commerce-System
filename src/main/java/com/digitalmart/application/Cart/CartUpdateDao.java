package com.digitalmart.application.Cart;

public record CartUpdateDao(
        Long productId,
        Integer newQuantity
) {
}
