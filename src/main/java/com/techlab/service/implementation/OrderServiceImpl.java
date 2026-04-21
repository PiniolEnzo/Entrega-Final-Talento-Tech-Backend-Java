package com.techlab.service.implementation;

import com.techlab.dto.order.OrderResponse;
import com.techlab.entity.Order;
import com.techlab.entity.Role;
import com.techlab.entity.User;
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
        return OrderMapper.toOrderResponse(orderRepository.findOrdersWithLines().orElseThrow(OrderNotFoundException::new));
    }

    @Override
    public OrderResponse getOrder(Long orderId) {
        Order order = orderRepository.findOrderWithLines(orderId).orElseThrow(OrderNotFoundException::new);
        return OrderMapper.toOrderResponse(order);
    }

    @Override
    public List<OrderResponse> getCurrentUserOrders() {
        User currentUser = authService.getCurrentUser();
        return OrderMapper.toOrderResponse(orderRepository.findByCustomerId(currentUser.getId()));
    }
}