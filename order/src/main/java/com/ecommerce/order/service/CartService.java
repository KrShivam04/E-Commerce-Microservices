package com.ecommerce.order.service;
import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.model.CartItem;

import java.util.List;

public interface CartService
{
    boolean addToCart(String userId, CartItemRequest request);

    boolean deleteItemFromCart(Long productId, String userId);

    List<CartItem> getCart(String userId);

    void clearCart(String userId);
}
