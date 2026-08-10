package com.digitalmart.application.Cart;

import com.digitalmart.application.Utils.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/items")
    public ResponseEntity<CartResponseDto> getCart(
            @AuthenticationPrincipal UserPrincipal currentUser
            ){

        Long userId = currentUser.getUserId();

        return new ResponseEntity<>(cartService.getCartItemsByUserId(userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Cart> addToCart(
            @RequestBody CartDao dao,
            @AuthenticationPrincipal UserPrincipal currentUser
    ){
        Long userId = currentUser.getUserId();
        return new ResponseEntity<>(cartService.addToCart(userId, dao.id(), 1), HttpStatus.OK);
    }

    @PutMapping("/items")
    public ResponseEntity<String> updateQuantity(
            @RequestBody CartUpdateDao dao,
            @AuthenticationPrincipal UserPrincipal currentUser
    ){
        Long userId = currentUser.getUserId();
        return new ResponseEntity<>(cartService.updateCartItemQuantity(userId, dao), HttpStatus.OK);
    }


    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Map<String, String>> removeItem(
            @PathVariable Long productId,
            @AuthenticationPrincipal UserPrincipal currentUser
    ){
        Long userId = currentUser.getUserId();
        return new ResponseEntity<>(cartService.removeFromCart(userId, productId), HttpStatus.OK);
    }
}
