package com.ecommerce.project.service;

import com.ecommerce.project.dto.OrderItemDTO;
import com.ecommerce.project.dto.OrderResponse;
import com.ecommerce.project.model.*;
import com.ecommerce.project.repository.OrderRepository;
import com.ecommerce.project.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final CartService cartService;
    private final OrderRepository orderRepository;

    public Optional<OrderResponse> createOrder(String userId) {

        List<CartItem> cartItems = cartService.getCartItems(userId);
        if (cartItems.isEmpty()) return Optional.empty();

        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
        if (userOpt.isEmpty()) return Optional.empty();

        User user = userOpt.get();

        //Total price
        BigDecimal totalPrice =
//                cartItems.stream().map(CartItem::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
                cartItems.stream().map(
                        cartItem ->
                                cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()))
                ).reduce(BigDecimal.ZERO, BigDecimal::add);

        //Create Order

        Order order = new Order();
        order.setStatus(OrderStatus.CONFIRMED);
        order.setUser(user);
        order.setTotalAmount(totalPrice);
        List<OrderItem> orderItems = cartItems.stream().map(
                cartItem -> new OrderItem(
                        null,
                        cartItem.getProduct(),
                        cartItem.getQuantity(),
                        cartItem.getPrice(),
                        order
                )
        ).toList();
        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        //Clear cart
        cartService.clearCart(userId);

        return Optional.of(mapOrderToOrderResponse(savedOrder));
    }

    private OrderResponse mapOrderToOrderResponse(Order savedOrder) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(savedOrder.getId());
        orderResponse.setStatus(savedOrder.getStatus());
        orderResponse.setCreatedAt(savedOrder.getCreatedAt());
        orderResponse.setTotalAmount(savedOrder.getTotalAmount());
        List<OrderItem> orderItems = savedOrder.getItems();
        List<OrderItemDTO> orderItemDTOList = getOrderItemDTOS(orderItems);
        orderResponse.setItems(orderItemDTOList);
        return orderResponse;
    }

    private List<OrderItemDTO> getOrderItemDTOS(List<OrderItem> orderItems) {
        List<OrderItemDTO> orderItemDTOList = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            OrderItemDTO orderItemDTO = new OrderItemDTO();
            orderItemDTO.setId(orderItem.getId());
            orderItemDTO.setPrice(orderItem.getPrice());
            orderItemDTO.setQuantity(orderItem.getQuantity());
            orderItemDTO.setSubTotal(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
            orderItemDTO.setProductId(orderItem.getProduct().getId());
            orderItemDTOList.add(orderItemDTO);
        }
        return orderItemDTOList;
    }
}
