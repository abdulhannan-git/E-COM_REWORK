package com.ecommerce.project.dto;

import com.ecommerce.project.model.OrderStatus;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private List<OrderItemDTO> items;
    @CreationTimestamp
    private LocalDateTime createdAt;
}
