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
 * Represents a customer order within the system.
 * <p>
 * Each order is associated with a user (customer), contains multiple order lines
 * (individual products and quantities), and has a payment status and total amount.
 * <p>
 * This class is mapped to the "orders" table in the database. It includes fields for:
 * <ul>
 *     <li>Unique order id</li>
 *     <li>Customer reference</li>
 *     <li>List of order lines (one-to-many relationship)</li>
 *     <li>Total amount of the order</li>
 *     <li>Payment status (using an enum)</li>
 *     <li>Timestamps for creation and last update</li>
 * </ul>
 * The order lines are represented by the {@link OrderLine} entity,
 * which contains details about each product in the order, including the product.
 * The {@code orderLines} list is lazily loaded to optimize performance.
 * <p>
 */

@Getter @Setter
@Entity
@Schema(description = "Represents a customer's order, including items, status, and total amount.")
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "Unique identifier of the order.", example = "1001")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @Schema(description = "Customer who placed the order.")
    private User customer;

    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    @Schema(description = "Current payment status of the order.", example = "PENDING")
    private PaymentStatus paymentStatus;

    @Column(name = "total")
    @Schema(description = "Total amount of the order.", example = "1499.99")
    private Float total;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @Schema(description = "Set of order lines that belong to this order.")
    private Set<OrderLine> lines = new LinkedHashSet<>();

    @CreationTimestamp
    @Schema(description = "Timestamp when the order was created.")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Schema(description = "Timestamp when the order was last updated.")
    private LocalDateTime updatedAt;

    /**
     * Creates a new Order instance from a ShoppingCart.
     * <p>
     * This method initializes an Order with the customer, payment status,
     * and total price from the provided ShoppingCart. It also populates
     * the order lines based on the items in the cart.
     *
     * @param customer The user who placed the order.
     * @param cart The shopping cart containing items to be included in the order.
     * @return A new Order instance populated with details from the shopping cart.
     */
    public static Order orderFromShoppingCart(User customer, ShoppingCart cart){
        Order order = new Order();

        order.setCustomer(customer);
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setTotal(cart.getTotalPrice());

        // para no crear ordenes vacias, a causa de un carrito sin item,
        // se usa una excepción cartEmptyException para lanzarla al recibir
        // el carrito sin items
        cart.getItems().forEach(item ->{
            OrderLine orderLine = new OrderLine(order, item.getProduct(), item.getQuantity());
            order.lines.add(orderLine);
        });

        return order;
    }

    /**
     * Checks if the order was placed by a specific customer.
     *
     * @param customer The user to check against the order's customer.
     * @return {@code true} if the order was placed by the specified customer, {@code false} otherwise.
     */
    public boolean isPlacedBy(User customer){
        return this.customer.equals(customer);
    }
}
