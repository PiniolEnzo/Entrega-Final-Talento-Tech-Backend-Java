package com.techlab.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


/**
 * Represents an item within a shopping cart, linking a product to a specific quantity.
 */
@Getter @Setter
@Entity
@Table(name = "cart_items")
@Schema(description = "Represents an item added to the shopping cart, including product and quantity.")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "Unique identifier of the cart item.", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @Schema(description = "The shopping cart to which this item belongs.")
    private ShoppingCart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @Schema(description = "Product associated with this cart item.")
    private Product product;

    @Column(name = "quantity")
    @Schema(description = "Quantity of the product in the cart.", example = "2")
    private int quantity;

    /**
     * Calculates the subtotal price for this cart item based on the product's price and quantity.
     *
     * @return the subtotal price of the cart item.
     */
    public Float getSubtotalPrice(){
        return product != null ? product.getPrice() * quantity : 0f;
    }

    @CreationTimestamp
    @Schema(description = "Timestamp when the cart item was created.")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Schema(description = "Timestamp when the cart item was last updated.")
    private LocalDateTime updatedAt;

}
