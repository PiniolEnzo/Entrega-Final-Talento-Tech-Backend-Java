package com.techlab.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Entity representing a user's shopping cart.
 * <p>
 * A {@code ShoppingCart} contains a collection of {@link CartItem} objects,
 * each representing a product and its quantity.
 * <p>
 * The cart is automatically timestamped when created and updated.
 * <p>
 * Each user has exactly one cart ({@code user_id} has a unique constraint).
 */

@Getter @Setter
@Entity
@Schema(description = "Represents a user's shopping cart containing selected products.")
@Table(name = "carts", uniqueConstraints = @UniqueConstraint(columnNames = "user_id", name = "uk_carts_user"))
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "Unique identifier for the shopping cart.", example = "1")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "User who owns this cart.")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.EAGER)
    @Schema(description = "Set of items currently in the shopping cart.")
    private Set<CartItem> items = new LinkedHashSet<>();

    @CreationTimestamp
    @Schema(description = "Timestamp when the cart was created.")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Schema(description = "Timestamp when the cart was last updated.")
    private LocalDateTime updatedAt;

    /**
     * Returns the total price of all items in the cart.
     * <p>
     * This method calculates the sum of the subtotal prices of each item in the cart.
     *
     * @return Total price of items in the cart as {@code Float}.
     */
    public Float getTotalPrice(){
        return items.stream()
                .map(CartItem::getSubtotalPrice)
                .reduce(0f,Float::sum);
    }

    /**
     * Retrieves a cart item by its product ID.
     * <p>
     * This method searches for an item in the cart that matches the given product ID.
     *
     * @param productId The ID of the product to find in the cart.
     * @return The {@link CartItem} if found, otherwise {@code null}.
     */
    public CartItem getItem(Long productId){
        return  items.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Adds a product to the cart. If it already exists, increments the quantity.
     *
     * @param product Product to add.
     * @return The resulting {@link CartItem}.
     */
    public CartItem addItem(Product product){
        CartItem item = getItem(product.getId());

        if (item != null){
            item.setQuantity(item.getQuantity() + 1);
        }else {
            CartItem itemNuevo = new CartItem();
            itemNuevo.setProduct(product);
            itemNuevo.setQuantity(1);
            itemNuevo.setCart(this);
            items.add(itemNuevo);
            return itemNuevo;
        }

        return item;
    }

    /**
     * Removes an item from the cart by its product ID.
     * <p>
     * If the item exists, it is removed from the cart and its reference to the cart is cleared.
     * <p>
     * @param productId The ID of the product to remove from the cart.
     */
    public void removeItem(Long productId){
        CartItem item = getItem(productId);
        if (item!=null){
            items.remove(item);
            item.setCart(null);
        }
    }

    /**
     * Clears all items from the cart.
     * <p>
     * This method removes all {@link CartItem} objects from the cart and clears their references to the cart.
     */
    public void clearItems(){
        items.forEach(item -> item.setCart(null));
        items.clear();
    }

    /**
     * Checks if the shopping cart is empty.
     *
     * @return {@code true} if there are no items in the cart, otherwise {@code false}.
     */
    public boolean isEmpty(){
        return items.isEmpty();
    }

    /**
     * Checks if the cart belongs to a specific user.
     *
     * @param user The user to check ownership against.
     * @return {@code true} if the cart belongs to the user, {@code false} otherwise.
     */
    public boolean belongsTo(User user) {
        return this.user != null && this.user.equals(user);
    }

}



