package com.digitalmart.application.Order;

import com.digitalmart.application.Utils.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Void> createOrder(
            @RequestBody OrderDao dao,
            @AuthenticationPrincipal UserPrincipal currentUser
            ){

        Long userId = currentUser.getUserId();

//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(orderService.createOrder(userId, dao));
        return null;
    }
}
