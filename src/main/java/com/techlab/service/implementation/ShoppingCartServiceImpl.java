package com.techlab.service.implementation;

import com.techlab.dto.shoppingCart.CartItemResponse;
import com.techlab.dto.shoppingCart.CartResponse;
import com.techlab.entity.CartItem;
import com.techlab.entity.Product;
import com.techlab.entity.ShoppingCart;
import com.techlab.exception.CartNotFoundException;
import com.techlab.exception.ProductNotFoundException;
import com.techlab.mapper.CartItemMapper;
import com.techlab.mapper.ShoppingCartMapper;
import com.techlab.repository.IShoppingCartRepository;
import com.techlab.service.IProductService;
import com.techlab.service.IShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("shoppingCartService")
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements IShoppingCartService {

    private final IShoppingCartRepository shoppingCartRepository;
    private final IProductService productService;

    @Override
    public CartResponse getCart(Long cartId) {
        return ShoppingCartMapper.toCartResponse(shoppingCartRepository.getCartWithItems(cartId).orElseThrow(CartNotFoundException::new));
    }

    @Override
    public ShoppingCart getShoppingCart(Long cartId){
        return shoppingCartRepository.getCartWithItems(cartId).orElseThrow(CartNotFoundException::new);
    }

    @Override
    public CartResponse createCart() {
        ShoppingCart cart = new ShoppingCart();
        shoppingCartRepository.save(cart);
        return ShoppingCartMapper.toCartResponse(cart);
    }

    @Override
    public CartItemResponse addToCart(Long cartId, Long productId) {
        ShoppingCart cart = getShoppingCart(cartId);

        Product product = productService.get(productId);

        CartItem cartItem = cart.addItem(product);

        shoppingCartRepository.save(cart);

        return CartItemMapper.toCartItemResponse(cartItem);
    }

    @Override
    public CartItemResponse updateItem(Long cartId, Long productId, Integer quantity) {
        ShoppingCart cart = getShoppingCart(cartId);

        CartItem cartItem = cart.getItem(productId);

        if (cartItem == null){
            throw new ProductNotFoundException();
        }

        cartItem.setQuantity(quantity);

        shoppingCartRepository.save(cart);

        return CartItemMapper.toCartItemResponse(cartItem);
    }

    @Override
    public void removeItem(Long cartId, Long productId) {
        ShoppingCart cart = getShoppingCart(cartId);

        cart.removeItem(productId);

        shoppingCartRepository.save(cart);
    }

    @Override
    public void clearCart(Long cartId) {
        ShoppingCart cart = getShoppingCart(cartId);

        cart.clear();

        shoppingCartRepository.save(cart);
    }
}
