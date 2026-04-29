package com.techlab.controller;

import com.techlab.dto.order.OrderResponse;
import com.techlab.service.IOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Tag(name = "Orders", description = "Operations for order management")
public class OrderController {

    private final IOrderService orderService;

    @Operation(
            summary = "Get my orders",
            description = "Retrieve a list of orders placed by the current authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully",
                    content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid")
    })
    @GetMapping("/my-orders")
    public List<OrderResponse> getMyOrders() {
        return orderService.getCurrentUserOrders();
    }

    @Operation(
            summary = "Get all orders (ADMIN)",
            description = "Retrieve a list of all orders in the system. Requires JWT authentication with ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully",
                    content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role")
    })
    @GetMapping
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }

    @Operation(
            summary = "Get order by ID",
            description = "Retrieve a specific order by its unique identifier. Requires JWT authentication with ADMIN role.",
            parameters = {
                    @Parameter(name = "orderId", description = "ID of the order to retrieve", required = true, example = "2")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found",
                    content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{orderId}")
    public OrderResponse getOrder(@PathVariable("orderId") Long orderId) {
        return orderService.getOrder(orderId);
    }

    @PutMapping("/{orderId}/status")
    @Operation(
            summary = "Update order status",
            description = "Update the payment status of an order. Requires JWT authentication with ADMIN role.",
            parameters = {
                    @Parameter(name = "orderId", description = "ID of the order to update", required = true, example = "2"),
                    @Parameter(name = "status", description = "New payment status (e.g., PAID, PENDING, FAILED)", required = true, example = "PAID")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated successfully",
                    content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid status value"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public OrderResponse updateOrderStatus(@PathVariable("orderId") Long orderId, @RequestParam("status") String status) {
        return orderService.updateOrderStatus(orderId, status);
    }

}
