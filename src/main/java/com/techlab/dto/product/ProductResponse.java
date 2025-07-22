package com.techlab.dto.product;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private Float price;
    private String imageUrl;
    private Short categoryId;
    private String categoryName;
}
