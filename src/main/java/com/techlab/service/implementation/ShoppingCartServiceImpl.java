package com.techlab.service.implementation;

import com.techlab.dto.shoppingCart.CartItemResponse;
import com.techlab.dto.shoppingCart.CartResponse;
import com.techlab.entity.CartItem;
import com.techlab.entity.Product;
import com.techlab.entity.ShoppingCart;
import com.techlab.entity.User;
import com.techlab.exception.CartNotFoundException;
import com.techlab.exception.ProductNotFoundException;
import com.techlab.mapper.CartItemMapper;
import com.techlab.mapper.ShoppingCartMapper;
import com.techlab.repository.IShoppingCartRepository;
import com.techlab.service.IAuthService;
import com.techlab.service.IProductService;
import com.techlab.service.IShoppingCartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service("shoppingCartService")
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements IShoppingCartService {

    private final IShoppingCartRepository shoppingCartRepository;
    private final IProductService productService;
    private final IAuthService authService;

    @Override
    public CartResponse getCart(Long cartId) {
        ShoppingCart cart = getShoppingCart(cartId);
        validateOwnership(cart);
        return ShoppingCartMapper.toCartResponse(cart);
    }

    @Transactional
    @Override
    public CartResponse createCart() {
        User user = authService.getCurrentUser();

        // Si el usuario ya tiene carrito, devolver el existente (idempotente)
        return shoppingCartRepository.findFirstByUser(user)
                .map(ShoppingCartMapper::toCartResponse)
                .orElseGet(() -> {
                    ShoppingCart cart = new ShoppingCart();
                    cart.setUser(user);
                    shoppingCartRepository.save(cart);
                    return ShoppingCartMapper.toCartResponse(cart);
                });
    }

    @Transactional
    @Override
    public CartResponse getCurrentUserCart() {
        User user = authService.getCurrentUser();

        return shoppingCartRepository.findFirstByUser(user)
                .map(ShoppingCartMapper::toCartResponse)
                .orElseGet(() -> {
                    ShoppingCart cart = new ShoppingCart();
                    cart.setUser(user);
                    shoppingCartRepository.save(cart);
                    return ShoppingCartMapper.toCartResponse(cart);
                });
    }

    @Transactional
    @Override
    public CartItemResponse addToCart(Long cartId, Long productId) {
        ShoppingCart cart = getShoppingCart(cartId);

        validateOwnership(cart);

        Product product = productService.get(productId);

        CartItem cartItem = cart.addItem(product);

        shoppingCartRepository.save(cart);

        return CartItemMapper.toCartItemResponse(cartItem);
    }

    @Transactional
    @Override
    public CartItemResponse updateItem(Long cartId, Long productId, Integer quantity) {
        ShoppingCart cart = getShoppingCart(cartId);

        validateOwnership(cart);

        CartItem cartItem = cart.getItem(productId);

        if (cartItem == null){
            throw new ProductNotFoundException();
        }

        cartItem.setQuantity(quantity);

        shoppingCartRepository.save(cart);

        return CartItemMapper.toCartItemResponse(cartItem);
    }

    @Transactional
    @Override
    public void removeItem(Long cartId, Long productId) {
        ShoppingCart cart = getShoppingCart(cartId);
        validateOwnership(cart);
        cart.removeItem(productId);

        shoppingCartRepository.save(cart);
    }

    @Transactional
    @Override
    public void clearCart(Long cartId) {
        ShoppingCart cart = getShoppingCart(cartId);
        validateOwnership(cart);
        cart.clearItems();

        shoppingCartRepository.save(cart);
    }

    private ShoppingCart getShoppingCart(Long cartId){
        return shoppingCartRepository.getCartWithItems(cartId).orElseThrow(CartNotFoundException::new);
    }

    private void validateOwnership(ShoppingCart cart){
        User currentUser = authService.getCurrentUser();
        if (currentUser==null){
            throw new AccessDeniedException("Authentication required");
        }
        if (!cart.belongsTo(currentUser)){
            throw new AccessDeniedException("You do not have permission to access this cart");
        }
    }
}
