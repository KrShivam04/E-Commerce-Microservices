package com.ecommerce.order.service;

import com.ecommerce.order.dto.OrderResponse;

import java.util.Optional;

public interface OrderService
{

    Optional<OrderResponse> createOrder(String userId);
}
