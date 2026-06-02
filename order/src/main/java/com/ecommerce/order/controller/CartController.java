package com.ecommerce.order.controller;
import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<String> addToCart(@RequestHeader("X-User-ID") String userId, @RequestBody CartItemRequest request) {
        if (cartService.addToCart(userId, request)) {
            return new ResponseEntity<>("Product added to cart", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Product out of stock or user not found !", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<String> removeFromCart(@RequestHeader("X-User-ID") String userId, @PathVariable Long productId) {
        boolean deleted = cartService.deleteItemFromCart(productId, userId);
        return deleted ? new ResponseEntity<>("Cart Item Deleted successfully", HttpStatus.OK) :
                new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getItem(@RequestHeader("X-User-ID") String userId) {
        return new ResponseEntity<>(cartService.getCart(userId), HttpStatus.OK);
    }

}
