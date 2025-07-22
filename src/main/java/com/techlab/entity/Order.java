package com.techlab.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter @Setter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "total")
    private Float total;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<OrderLine> lines = new LinkedHashSet<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

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

    public boolean isPlacedBy(User customer){
        return this.customer.equals(customer);
    }
}
