package com.techlab.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Order response DTO")
public class OrderResponse {
    @Schema(description = "Order ID", example = "1")
    private Long id;
    @Schema(description = "Payment status", example = "COMPLETED")
    private String paymentStatus;
    @Schema(description = "Order creation timestamp")
    private LocalDateTime createdAt;
    @Schema(description = "List of order line items")
    private List<OrderLineResponse> items;
    @Schema(description = "Total order price", example = "1499.99")
    private Float totalPrice;
}
