package com.ecommerce.order.service;

import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService
{
    //private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    //private final UserRepository userRepository;

    @Override
    public boolean addToCart(String userId, CartItemRequest request)
    {
//        Optional<Product> productOpt = productRepository.findById(request.getProductId());
//        if (productOpt.isEmpty())
//        {
//            return false;
//        }
//        Product product = productOpt.get();
//        if (product.getStockQuantity() < request.getQuantity())
//        {
//            return false;
//        }
//        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
//        if (userOpt.isEmpty())
//        {
//            return false;
//        }
//        User user = userOpt.get();
        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId, String.valueOf(request.getProductId()));
        if (existingCartItem != null)
        {
            // item exist in cart
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(BigDecimal.valueOf(1000)); // need to chnage by interservice communication
            cartItemRepository.save(existingCartItem);
        }
        else
        {
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

    @Transactional
    @Override
    public boolean deleteItemFromCart(Long productId, String userId)
    {
//        Optional<Product> productOpt = productRepository.findById(productId);
//        if (productOpt.isEmpty())
//        {
//            return false;
//        }
//        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
//        if (userOpt.isEmpty())
//        {
//            return false;
//        }

        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, String.valueOf(productId));
        if (cartItem != null)
        {
            cartItemRepository.deleteByUserIdAndProductId(userId, String.valueOf(productId));
            return true;
        }
        return false;
    }

    @Override
    public List<CartItem> getCart(String userId)
    {
        return cartItemRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public void clearCart(String userId)
    {
        cartItemRepository.deleteByUserId(userId);
    }
}
