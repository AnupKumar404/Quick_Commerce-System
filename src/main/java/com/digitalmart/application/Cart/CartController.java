package com.digitalmart.application.Cart;

import com.digitalmart.application.Utils.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/items")
    public ResponseEntity<List<CartItem>> getCart(
            @AuthenticationPrincipal UserPrincipal currentUser
            ){

        Long userId = currentUser.getUserId();

        return new ResponseEntity<>(cartService.getCartByUserId(userId).getItems(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Cart> addToCart(
            @RequestBody CartDao dao,
            @AuthenticationPrincipal UserPrincipal currentUser
    ){
        Long userId = currentUser.getUserId();
        return new ResponseEntity<>(cartService.addToCart(userId, dao.productId(), dao.quantity()), HttpStatus.OK);
    }
}
