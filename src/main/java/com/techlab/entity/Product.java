package com.techlab.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Represents a product available in the system.
 * <p>
 * Each product belongs to a specific category and contains details
 * such as name, description, price, stock, image URL, and creation/update timestamps.
 * <p>
 * This entity is mapped to the "products" table in the database.
 * The relationship to {@link Category} is many-to-one, allowing multiple products
 * to be grouped under the same category.
 * <p>
 */

@Getter @Setter
@Entity
@Schema(description = "Represents a product available for purchase.")
@Table(name = "products")
@Builder
@AllArgsConstructor @NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "Unique identifier of the product.", example = "101")
    private Long id;

    @Column(name = "name")
    @Schema(description = "Name of the product.", example = "Motorola moto Edge 50 Pro")
    private String name;

    @Column(name = "description")
    @Schema(description = "Detailed description of the product.", example = "Smartphone with 512GB storage and 12GB RAM with a 200MP camera and 165Hz refresh rate display with 6.7 inches, 1440 x 3120 pixels resolution, powered by Snapdragon 7 Gen 3 Octa Core 2.63GHz processor and a battery capacity of 4500mAh.")
    private String description;

    @Column(name = "sale_price")
    @Schema(description = "Unit price of the product.", example = "1130000.00")
    private Float price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    @Schema(description = "Category to which the product belongs.")
    private Category category;

    @Column(name = "stock")
    @Schema(description = "Available stock quantity of the product.", example = "100")
    private Integer stock;

    @Column(name = "image_url")
    @Schema(description = "URL of the product's image.", example = "https://http2.mlstatic.com/D_NQ_NP_993519-MLA80895362030_122024-O.webp")
    private String imageUrl;

    @CreationTimestamp
    @Schema(description = "Timestamp when the product was created.")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Schema(description = "Timestamp when the product was last updated.")
    private LocalDateTime updatedAt;
}
