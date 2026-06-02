package com.ecommerce.order.service;

import com.ecommerce.order.clients.ProductServiceClient;
import com.ecommerce.order.clients.UserServiceClient;
import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.dto.ProductResponse;
import com.ecommerce.order.dto.UserResponse;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.repository.CartItemRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartItemRepository cartItemRepository;
    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;


    @Override
    @CircuitBreaker(name = "productService", fallbackMethod = "addToCartFallBack")
    public boolean addToCart(String userId, CartItemRequest request) {
        ProductResponse product = productServiceClient.getProductById(request.getProductId());
        if (product == null) {
            return false;
        }
        if (product.getStockQuantity() < request.getQuantity()) {
            return false;
        }
        UserResponse user = userServiceClient.getUserById(userId);
        if (user == null) {
            return false;
        }

        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId, String.valueOf(request.getProductId()));
        if (existingCartItem != null) {
            // item exist in cart
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(BigDecimal.valueOf(1000)); // need to change in interservice communication
            cartItemRepository.save(existingCartItem);
        } else {
            // item does not exist in cart
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(String.valueOf(request.getProductId()));
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(BigDecimal.valueOf(1000));
            cartItemRepository.save(cartItem);
        }
        return true;
    }

    public boolean addToCartFallBack(String userId, CartItemRequest request, Exception exception) {
        exception.printStackTrace();
        return false;
    }

    @Transactional
    @Override
    public boolean deleteItemFromCart(Long productId, String userId) {
        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, String.valueOf(productId));
        if (cartItem != null) {
            cartItemRepository.deleteByUserIdAndProductId(userId, String.valueOf(productId));
            return true;
        }
        return false;
    }

    @Override
    public List<CartItem> getCart(String userId) {
        return cartItemRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
