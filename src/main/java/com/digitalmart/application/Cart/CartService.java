package com.digitalmart.application.Cart;

import com.digitalmart.application.Product.Product;
import com.digitalmart.application.Product.ProductRepository;
import com.digitalmart.application.UserIdentity.User;
import com.digitalmart.application.UserIdentity.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;


    /* Different methods to do operations on cart */

    // Fetches the user's cart. Creates an empty one if it doesn't exist.

    @Transactional
    public CartResponseDto getCartItemsByUserId(Long userId) {

        Cart existingCart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createEmptyCart(userId));

        List<CartItem> cartItems = cartItemRepository.findByCartId(existingCart.getId())
                .orElseThrow();

        List<CartItemResponseDto> itemDTOs = new ArrayList<>();

        BigDecimal total = BigDecimal.ZERO;

        for(CartItem item : cartItems){
            Product product = item.getProduct();

            CartItemResponseDto dto = new CartItemResponseDto(
                    product.getId(),
                    product.getName(),
                    product.getImageUrl(),
                    item.getQuantity(),
                    product.getDescription(),
                    product.getUnitType(),
                    product.getUnitValue(),
                    product.getPrice(),
                    product.getPrice()
            );

            itemDTOs.add(dto);
            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        return new CartResponseDto(
                existingCart.getId(),
                itemDTOs,
                total
        );
    }



    // Get the cart of current user

    @Transactional
    public Cart getCartByUserId(Long userId){
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> createEmptyCart(userId));
    }


    // Create cart for newer user

    @Transactional
    public Cart createEmptyCart(Long userId) {
        User user = userRepository.getReferenceById(userId);

        Cart newCart = Cart.builder()
                .user(user)
                .build();
        return cartRepository.save(newCart);
    }


    // Adds a product to the cart or increments its quantity if it already exists.

    @Transactional
    public Cart addToCart(Long userId, Long productId, Integer quantityToAdd) {

        if (quantityToAdd <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createEmptyCart(userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

        // Edge Case: Check if item is already in the cart
        Optional<CartItem> existingItemOpt = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            int newTotalQuantity = existingItem.getQuantity() + quantityToAdd;

            // Edge Case: Check if the COMBINED quantity exceeds available Stock
            if (newTotalQuantity > product.getAvailableStock()) {
                throw new IllegalStateException("Not enough availableStock available. You requested "
                        + newTotalQuantity + " but only " + product.getAvailableStock() + " are available.");
            }
            existingItem.setQuantity(newTotalQuantity);
        } else {
            // Edge Case: Check availableStock for new item
            if (quantityToAdd > product.getAvailableStock()) {
                throw new IllegalStateException("Not enough availableStock available.");
            }
            cart.addItem(product, quantityToAdd);
        }

        // Saving the cart will cascade to save/update the CartItems
        return cartRepository.save(cart);
    }

    /**
     * Explicitly updates the quantity of a specific cart item.
     */
    @Transactional
    public String updateCartItemQuantity(Long userId, CartUpdateDao updateDao) {
        Cart cart = getCartByUserId(userId);

        CartItem cartItem = cartItemRepository.findByProductIdAndCartId(updateDao.productId(), cart.getId())
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found."));

        // Edge Case: Prevent a user from modifying another user's cart item via API spoofing
        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new SecurityException("Unauthorized modification attempt.");
        }

        // Edge Case: If user reduces quantity to 0 or less, remove the item entirely
        if (updateDao.newQuantity() <= 0) {
            cart.removeItem(cartItem);
            cartRepository.save(cart);
            return "Removed Successfully..";
        }

        // Edge Case: Verify availableStock against the new absolute quantity
//        Product product = cartItem.getProduct();
//        if (updateDao.newQuantity() > product.getAvailableStock()) {
//            throw new IllegalStateException("Requested quantity exceeds available availableStock.");
//        }

        cartItem.setQuantity(updateDao.newQuantity());
        cartRepository.save(cart);
        return "Updated Successfully...";
    }


    // Removes an item from the cart entirely.

    @Transactional
    public Map<String, String> removeFromCart(Long userId, Long productId) {
        Cart cart = getCartByUserId(userId);

        CartItem cartItem = cartItemRepository.findByProductIdAndCartId(productId, cart.getId())
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found."));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new SecurityException("Unauthorized modification attempt.");
        }

        cart.removeItem(cartItem);
        cartRepository.save(cart);

        return Map.of("message", "Removed Successfully");
    }


    // Empties the entire cart (useful after a successful checkout).

    @Transactional
    public void clearCart(Long userId) {
        Cart cart = getCartByUserId(userId);
        cart.getItems().clear(); // Orphan removal will delete the CartItems from the DB
        cartRepository.save(cart);
    }
}
