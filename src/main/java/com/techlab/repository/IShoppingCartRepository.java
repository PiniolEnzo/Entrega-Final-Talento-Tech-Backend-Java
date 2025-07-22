package com.techlab.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import com.techlab.entity.ShoppingCart;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("shoppingCartRepository")
public interface IShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @EntityGraph(attributePaths = "items.product")
    @Query("SELECT sc FROM ShoppingCart sc WHERE sc.id = :cartId")
    Optional<ShoppingCart> getCartWithItems(@Param("cartId") Long cartId);
}

