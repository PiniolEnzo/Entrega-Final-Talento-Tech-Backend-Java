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
@Table(name = "carts")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<CartItem> items = new LinkedHashSet<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Float getTotalPrice(){
        return items.stream()
                .map(CartItem::getSubtotalPrice)
                .reduce(0f,Float::sum);
    }

    public CartItem getItem(Long productId){
        return  items.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    public CartItem addItem(Product product){
        CartItem item = getItem(product.getId());

        if (item != null){
            item.setQuantity(item.getQuantity() + 1);
        }else {
            item = new CartItem();
            item.setProduct(product);
            item.setQuantity(1);
            item.setCart(this);
            items.add(item);
        }

        return item;
    }

    public void removeItem(Long productId){
        CartItem item = getItem(productId);
        if (item!=null){
            items.remove(item);
            item.setCart(null);
        }
    }

    public void clear(){
        items.clear();
    }

    public boolean isEmpty(){
        return items.isEmpty();
    }
}
