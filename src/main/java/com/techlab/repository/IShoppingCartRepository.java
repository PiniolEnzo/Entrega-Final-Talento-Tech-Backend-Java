package com.techlab.repository;

import com.techlab.entity.ShoppingCart;
import com.techlab.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("shoppingCartRepository")
public interface IShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @EntityGraph(attributePaths = "items.product")
    @Query("SELECT sc FROM ShoppingCart sc WHERE sc.id = :cartId")
    Optional<ShoppingCart> getCartWithItems(@Param("cartId") Long cartId);

    @EntityGraph(attributePaths = "items.product")
    @Query("SELECT sc FROM ShoppingCart sc WHERE sc.user = :user")
    Optional<ShoppingCart> findFirstByUser(@Param("user") User user);
}

