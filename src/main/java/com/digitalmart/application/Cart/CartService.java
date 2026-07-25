package com.digitalmart.application.Cart;

import com.digitalmart.application.Product.Product;
import com.digitalmart.application.Product.ProductRepository;
import com.digitalmart.application.UserIdentity.User;
import com.digitalmart.application.UserIdentity.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository; // Assuming you have this to link Carts to Users

    /**
     * Fetches the user's cart. Creates an empty one if it doesn't exist.
     */
    @Transactional
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> createEmptyCart(userId));
    }

    @Transactional
    public Cart createEmptyCart(Long userId) {
        User user = userRepository.getReferenceById(userId);

        Cart newCart = Cart.builder()
                .user(user)
                .build();
        return cartRepository.save(newCart);
    }

    /**
     * Adds a product to the cart or increments its quantity if it already exists.
     */
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

            // Edge Case: Check if the COMBINED quantity exceeds available availableStock
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
    public Cart updateCartItemQuantity(Long userId, Long cartItemId, Integer newQuantity) {
        Cart cart = getCartByUserId(userId);

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found."));

        // Edge Case: Prevent a user from modifying another user's cart item via API spoofing
        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new SecurityException("Unauthorized modification attempt.");
        }

        // Edge Case: If user reduces quantity to 0 or less, remove the item entirely
        if (newQuantity <= 0) {
            cart.removeItem(cartItem);
            return cartRepository.save(cart);
        }

        // Edge Case: Verify availableStock against the new absolute quantity
        Product product = cartItem.getProduct();
        if (newQuantity > product.getAvailableStock()) {
            throw new IllegalStateException("Requested quantity exceeds available availableStock.");
        }

        cartItem.setQuantity(newQuantity);
        return cartRepository.save(cart);
    }

    /**
     * Removes an item from the cart entirely.
     */
    @Transactional
    public Cart removeFromCart(Long userId, Long cartItemId) {
        Cart cart = getCartByUserId(userId);

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found."));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new SecurityException("Unauthorized modification attempt.");
        }

        cart.removeItem(cartItem);
        return cartRepository.save(cart);
    }

    /**
     * Empties the entire cart (useful after a successful checkout).
     */
    @Transactional
    public void clearCart(Long userId) {
        Cart cart = getCartByUserId(userId);
        cart.getItems().clear(); // Orphan removal will delete the CartItems from the DB
        cartRepository.save(cart);
    }
}
