package com.digitalmart.application.Cart;

import com.digitalmart.application.Product.Product;
import com.digitalmart.application.UserIdentity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CartItem> items = new ArrayList<>();

    public void addItem(Product product, Integer quantity){
        CartItem cartItem = new CartItem(this, product, quantity);
        this.items.add(cartItem);
    }

    public void removeItem(CartItem cartItem){
        this.items.remove(cartItem);
        cartItem.setCart(null);
    }
}
