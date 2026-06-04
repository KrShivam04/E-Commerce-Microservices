package com.ecommerce.order.dto;

import com.ecommerce.order.constant.OrderStatus;
import com.ecommerce.order.model.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {

    private Long orderId;
    private String userID;
    private OrderStatus orderStatus;
    private BigDecimal totalAmount;
    private List<OrderItemDTO> items;
    private LocalDateTime orderDate;

}
