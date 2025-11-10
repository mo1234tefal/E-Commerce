package com.mo.ecommerce.service;

import com.mo.ecommerce.dto.order.OrderItemsDto;
import com.mo.ecommerce.dto.order.OrderRequestDto;
import com.mo.ecommerce.dto.order.OrderResponseDto;
import com.mo.ecommerce.dto.order.UpdateShippingDetailsDto;
import com.mo.ecommerce.entity.*;
import com.mo.ecommerce.enums.OrderStatus;
import com.mo.ecommerce.repository.CartRepo;
import com.mo.ecommerce.repository.OrderRepo;
import com.mo.ecommerce.repository.ProductRepo;
import jdk.jshell.Snippet;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepo orderRepo;
    private final ProductRepo productRepo;
    private final CartRepo cartRepo;


    @Transactional
    public OrderResponseDto checkoutSingleProduct(User user , Long productId , Integer quantity){
        Product product = productRepo.findById(productId).orElseThrow(() -> new RuntimeException("product is not found"));


        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock for product: " + product.getName());
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        OrderItem orderItem = OrderItem.builder()
                .product(product)
                .quantity(quantity)
                .order(order)
                .price(quantity * product.getPrice())
                .build();

        order.getOrderItems().add(orderItem);
        order.setTotalPrice(orderItem.getPrice());
        Order savedOrder = orderRepo.save(order);

        cartRepo.findByUser(user).ifPresent(cart -> {
            cart.getCartItems().removeIf(item -> item.getProduct().getId().equals(productId));
            cartRepo.save(cart);
        });

        return maptoOrderResponseDto(savedOrder);

    }

    // OrderService.java

    @Transactional
    public OrderResponseDto checkout(User user) {

        Cart cart = cartRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found for user."));

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty. Cannot checkout.");
        }

        List<CartItem> cartItems = cart.getCartItems();

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            Integer requestedQuantity = cartItem.getQuantity();


            if (product.getStock() < requestedQuantity) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName() +
                        ". Available: " + product.getStock() +
                        ", Requested: " + requestedQuantity);
            }


            product.setStock(product.getStock() - requestedQuantity);
            productRepo.save(product);
        }


        Order order = new Order();
        order.setUser(user);

        order.setTotalPrice(cart.getTotalPrice() != null ? cart.getTotalPrice() : 0.0);

        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> OrderItem.builder()
                        .product(cartItem.getProduct())
                        .order(order)
                        .quantity(cartItem.getQuantity())
                        .price(cartItem.getPrice())
                        .build())
                .collect(Collectors.toList());

        order.setOrderItems(orderItems);


        Order savedOrder = orderRepo.save(order);

        cart.getCartItems().clear();
        cart.setTotalPrice(0.0);
        cartRepo.save(cart);

        
        return maptoOrderResponseDto(savedOrder);
    }

    private OrderResponseDto maptoOrderResponseDto(Order order){
        return OrderResponseDto.builder()
                .username(order.getUser().getUsername())
                .totalPrice(order.getTotalPrice())
                .orderStatus(order.getStatus().name())
                .orderDate(order.getCreated())
                .orderItems(order.getOrderItems().stream().map(
                        this::mapToOrderItemsDto ).collect(Collectors.toList()))
                .shippingAddress(order.getShippingAddress())
                .trackingNumber(order.getTrackingNumber())
                .deliveredAt(order.getDeliveredAt())
                .shippedAt(order.getShippedAt())
                .build();
    }

    private OrderItemsDto mapToOrderItemsDto(OrderItem orderItem){
        return OrderItemsDto.builder()
                .productName(orderItem.getProduct().getName())
                .quantity(orderItem.getQuantity())
                .totalPrice(orderItem.getPrice())
                .build();
    }

    public List<OrderResponseDto> getAllOrders(User user){
        return orderRepo.findAllByUser(user)
                .stream()
                .map(this::maptoOrderResponseDto)
                .collect(Collectors.toList());
    }

    public OrderResponseDto getOrderById(Long id, User user){
        Order order = orderRepo.findById(id).orElseThrow(() -> new RuntimeException("order is not found"));

        if (!order.getUser().getId().equals(user.getId()) &&
                !user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {

            throw new RuntimeException("You are not authorized to view this order");
        }

        return maptoOrderResponseDto(order);
    }

    @Transactional
    public OrderResponseDto updateOrderStatus(Long orderId , OrderStatus newStatus){
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new RuntimeException("order is not found"));
        order.setStatus(newStatus);
        Order savedOrder = orderRepo.save(order);
        return maptoOrderResponseDto(savedOrder);
    }

    public List<OrderResponseDto> getAllOrders(){
        return orderRepo.findAll().stream()
                .map(this::maptoOrderResponseDto)
                .collect(Collectors.toList());
    }

    public OrderResponseDto cancelOrder(Long orderId , User user){
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new RuntimeException("order is not found"));
        if(!order.getUser().getId().equals(user.getId())){
            throw new RuntimeException("user is not permitted to cancel order");
        }
        if(!order.getStatus().equals(OrderStatus.PENDING)){
            throw new RuntimeException("order is not pending to cancel order");
        }
        order.setStatus(OrderStatus.CANCELED);
        order.getOrderItems()
                .forEach(item ->{
                    Product product = item.getProduct();
                    product.setStock(product.getStock() + item.getQuantity());
                    productRepo.save(product);}
                );
        return maptoOrderResponseDto(orderRepo.save(order));
    }

    @Transactional
    public OrderResponseDto returnOrder(User user, Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not authorized to return this order");
        }

        if (!order.getStatus().equals(OrderStatus.DELIVERED)) {
            throw new RuntimeException("Only delivered orders can be returned");
        }

        LocalDateTime deliveryDate = order.getUpdated();
        LocalDateTime now = LocalDateTime.now();
        if (deliveryDate.plusDays(14).isBefore(now)) {
            throw new RuntimeException("Return period expired. You can only return within 14 days of delivery.");
        }

        order.getOrderItems().forEach(item -> {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            productRepo.save(product);
        });

        order.setStatus(OrderStatus.RETURNED);
        Order savedOrder = orderRepo.save(order);

        return maptoOrderResponseDto(savedOrder);
    }
    @Transactional
    public OrderResponseDto updateShippingDetails(UpdateShippingDetailsDto shippingDetailsDto) {
        Order order = orderRepo.findById(shippingDetailsDto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setTrackingNumber(shippingDetailsDto.getTrackingNumber());
        order.setShippingAddress(shippingDetailsDto.getShippingAddress());
        order.setStatus(OrderStatus.SHIPPED);
        order.setShippedAt(LocalDateTime.now());

        Order saved = orderRepo.save(order);
        return maptoOrderResponseDto(saved);
    }

    @Transactional
    public OrderResponseDto markAsDelivered(Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(OrderStatus.DELIVERED);
        order.setDeliveredAt(LocalDateTime.now());

        return maptoOrderResponseDto(orderRepo.save(order));
    }





}
