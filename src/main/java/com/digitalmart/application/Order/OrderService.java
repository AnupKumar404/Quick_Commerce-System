package com.digitalmart.application.Order;

import com.digitalmart.application.Product.Product;
import com.digitalmart.application.Product.ProductRepository;
import com.digitalmart.application.UserIdentity.User;
import com.digitalmart.application.UserIdentity.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public String createOrder(Long userId, OrderDao dao){






        return null;
    }


}
