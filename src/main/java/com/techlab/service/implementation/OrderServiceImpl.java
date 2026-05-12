package com.techlab.service.implementation;
import com.techlab.dto.order.OrderResponse;
import com.techlab.entity.CartItem;
import com.techlab.entity.Order;
import com.techlab.entity.PaymentStatus;
import com.techlab.entity.Product;
import com.techlab.entity.ShoppingCart;
import com.techlab.entity.User;
import com.techlab.exception.CartEmptyException;
import com.techlab.exception.CartNotFoundException;
import com.techlab.exception.InsufficientStockException;
import com.techlab.exception.OrderNotFoundException;
import com.techlab.mapper.OrderMapper;
import com.techlab.repository.IOrderRepository;
import com.techlab.repository.IProductRepository;
import com.techlab.repository.IShoppingCartRepository;
import com.techlab.service.IAuthService;
import com.techlab.service.IOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service("orderService")
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {
    private final IOrderRepository orderRepository;
    private final IShoppingCartRepository shoppingCartRepository;
    private final IProductRepository productRepository;
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
    @Transactional
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

        // Reducir stock antes de crear la orden
        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            int quantity = item.getQuantity();

            if (product.getStock() == null || product.getStock() < quantity) {
                log.warn("Stock insuficiente para '{}': disponible={}, solicitado={}",
                        product.getName(), product.getStock(), quantity);
                throw new InsufficientStockException(product.getName());
            }

            product.setStock(product.getStock() - quantity);
            productRepository.save(product);
        }

        int itemCount = cart.getItems().size();

        Order order = orderRepository.save(Order.orderFromShoppingCart(currentUser, cart));

        cart.clearItems();
        shoppingCartRepository.save(cart);

        log.info("Checkout completado para usuario '{}' - orden #{}, {} items",
                currentUser.getName(), order.getId(), itemCount);

        return OrderMapper.toOrderResponse(order);
    }
}