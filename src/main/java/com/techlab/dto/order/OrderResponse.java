package com.techlab.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private String paymentStatus;
    private LocalDateTime createdAt;
    private List<OrderLineResponse> items;
    private Float totalPrice;
}
