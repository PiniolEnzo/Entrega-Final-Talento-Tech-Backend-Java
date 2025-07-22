package com.techlab.service.implementation;

import com.techlab.dto.order.OrderResponse;
import com.techlab.entity.Order;
import com.techlab.exception.OrderNotFoundException;
import com.techlab.mapper.OrderMapper;
import com.techlab.repository.IOrderRepository;
import com.techlab.service.IAuthService;
import com.techlab.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("orderService")
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {
    private final IOrderRepository orderRepository;
    private final IAuthService authService;

    @Override
    public List<OrderResponse> getAllOrders() {
        return OrderMapper.toOrderResponse(orderRepository.findOrdersByCustomer(authService.getCurrentUser()).orElseThrow(OrderNotFoundException::new));
    }

    @Override
    public OrderResponse getOrder(Long orderId) {
        Order order = orderRepository.findOrderWithLines(orderId).orElseThrow(OrderNotFoundException::new);

        if (!order.isPlacedBy(authService.getCurrentUser())){
            throw new AccessDeniedException("You don't have access to this order.");
        }

        return OrderMapper.toOrderResponse(order);
    }



}
