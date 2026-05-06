package com.techlab.service.implementation;
import com.techlab.dto.order.OrderResponse;
import com.techlab.entity.Order;
import com.techlab.entity.PaymentStatus;
import com.techlab.entity.ShoppingCart;
import com.techlab.entity.User;
import com.techlab.exception.CartEmptyException;
import com.techlab.exception.CartNotFoundException;
import com.techlab.exception.OrderNotFoundException;
import com.techlab.mapper.OrderMapper;
import com.techlab.repository.IOrderRepository;
import com.techlab.repository.IShoppingCartRepository;
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
    private final IShoppingCartRepository shoppingCartRepository;
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

    @Override
    public OrderResponse updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        order.setPaymentStatus(PaymentStatus.fromString(status.toUpperCase()));
        orderRepository.save(order);
        return OrderMapper.toOrderResponse(order);
    }

    @Override
    public OrderResponse checkout(Long cartId) {
        User currentUser = authService.getCurrentUser();
        ShoppingCart cart = shoppingCartRepository.getCartWithItems(cartId)
                .orElseThrow(CartNotFoundException::new);

        if (!cart.belongsTo(currentUser)) {
            throw new AccessDeniedException("Cart does not belong to current user");
        }

        if (cart.isEmpty()) {
            throw new CartEmptyException();
        }

        Order order = orderRepository.save(Order.orderFromShoppingCart(currentUser, cart));

        cart.clearItems();
        shoppingCartRepository.save(cart);

        return OrderMapper.toOrderResponse(order);
    }
}