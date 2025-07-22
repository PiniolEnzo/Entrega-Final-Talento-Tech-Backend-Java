package com.techlab.repository;

import com.techlab.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import com.techlab.entity.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("orderRepository")
public interface IOrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = "lines.product")
    @Query("SELECT o FROM Order o WHERE o.customer = :customer")
    Optional<List<Order>> findOrdersByCustomer(@Param("customer") User customer);

    @EntityGraph(attributePaths = "lines.product")
    @Query("SELECT o FROM Order o WHERE o.id = :orderId")
    Optional<Order> findOrderWithLines(@Param("orderId") Long orderId);

    @EntityGraph(attributePaths = "lines.product")
    @Query("SELECT o FROM Order o")
    Optional<List<Order>> findOrdersWithLines();
}

