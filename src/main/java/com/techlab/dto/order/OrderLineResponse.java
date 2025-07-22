package com.techlab.dto.order;

import com.techlab.dto.product.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineResponse {
    private ProductDto product;
    private int quantity;
    private Float subtotalPrice;
}
