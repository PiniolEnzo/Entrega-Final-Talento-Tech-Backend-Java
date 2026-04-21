package com.techlab.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a product category in the system.
 * Each category can contain multiple products.
 * <p>
 * This class is mapped to the "categories" table in the database.
 * It includes fields for the category's unique identifier, name,
 * and timestamps for creation and last update.
 * It also establishes a one-to-many relationship with the Product entity.
 * The {@code products} field is lazily loaded to optimize performance.
 * <p>
 */
@Getter @Setter
@NoArgsConstructor @ToString(exclude = "products")
@Entity
@Table(name = "categories")
@Schema(description = "Represents a category of products.")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "Unique identifier for the category.")
    private Short id;

    @OneToMany(fetch=FetchType.LAZY, mappedBy="category")
    @Schema(description = "Set of products associated with this category.")
    private Set<Product> products = new HashSet<>();

    @Column(name = "name")
    @Schema(description = "Name of the category.", example = "Electronics")
    private String name;

    /**
     * Constructs a new Category with the specified id and name.
     *
     * @param id the unique identifier for the category
     */
    public Category(short id) {
        this.id = id;
    }

    /**
     * Constructs a new Category with the specified name.
     *
     * @param name the name of the category
     */
    public Category(String name) {
        this.name = name;
    }

    @Schema(description = "Timestamp when the category was created.")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the category was last updated.")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
