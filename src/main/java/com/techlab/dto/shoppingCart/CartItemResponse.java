package com.techlab.dto.shoppingCart;

import com.techlab.dto.product.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class CartItemResponse {
    private ProductDto product;
    private int quantity;
    private Float subtotal;

}
