package com.techlab.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Represents a single line item within an order.
 * <p>
 * Each order line links a product to its quantity and subtotal within a specific order.
 * This allows the order to contain multiple products with their individual prices and quantities.
 * <p>
 * This class is mapped to the "order_lines" table in the database. It contains:
 * <ul>
 *     <li>An auto-generated ID</li>
 *     <li>References to the associated order</li>
 *     <li>References to the associated product</li>
 *     <li>Unit price of the product at the time of order</li>
 *     <li>Quantity of the product ordered</li>
 *     <li>Calculated subtotal price for this line</li>
 *     <li>Creation and last update timestamps</li>
 * </ul>
 */

@Getter @Setter
@NoArgsConstructor
@Entity
@Schema(description = "Represents a single line within an order, including product, quantity, and subtotal.")
@Table(name = "order_lines")
public class OrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "Unique identifier of the order line.", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @Schema(description = "Order to which this line belongs.")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @Schema(description = "Product included in this order line.")
    private Product product;

    @Column(name = "unit_price")
    @Schema(description = "Unit price of the product at the time of order.", example = "99.99")
    private Float unitPrice;

    @Column(name = "quantity")
    @Schema(description = "Quantity of the product ordered.", example = "3")
    private Integer quantity;

    @Column(name = "subtotal")
    @Schema(description = "Subtotal price for this line (product price * quantity).", example = "299.97")
    private Float subtotalPrice;

    @CreationTimestamp
    @Schema(description = "Timestamp when the order line was created.")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Schema(description = "Timestamp when the order line was last updated.")
    private LocalDateTime updatedAt;

    /**
     * Constructs a new OrderLine with the specified order, product, and quantity.
     * The unit price is set to the product's current price, and the subtotal is calculated.
     *
     * @param order    The order this line item belongs to
     * @param product  The product being ordered
     * @param quantity The quantity of the product ordered
     */
    public OrderLine(Order order, Product product, Integer quantity){
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = product.getPrice();
        this.subtotalPrice = this.unitPrice * quantity;
    }
}
