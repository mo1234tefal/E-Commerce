package com.mo.ecommerce.controller;

import com.mo.ecommerce.dto.order.OrderResponseDto;
import com.mo.ecommerce.dto.order.UpdateShippingDetailsDto;
import com.mo.ecommerce.entity.User;
import com.mo.ecommerce.enums.OrderStatus;
import com.mo.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @PostMapping("/checkout")
    public ResponseEntity<OrderResponseDto> checkout(@AuthenticationPrincipal User user) {
        OrderResponseDto order = orderService.checkout(user);
        return ResponseEntity.ok(order);
    }


    @PostMapping("/checkout/{productId}")
    public ResponseEntity<OrderResponseDto> checkoutSingleProduct(
            @AuthenticationPrincipal User user,
            @PathVariable Long productId,
            @RequestParam(defaultValue = "1") Integer quantity
    ) {
        OrderResponseDto order = orderService.checkoutSingleProduct(user, productId, quantity);
        return ResponseEntity.ok(order);
    }


    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponseDto>> getUserOrders(@AuthenticationPrincipal User user) {
        List<OrderResponseDto> orders = orderService.getAllOrders(user);
        return ResponseEntity.ok(orders);
    }


    @GetMapping("/{orderId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long orderId , User user) {
        return ResponseEntity.ok(orderService.getOrderById(orderId ,  user));
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponseDto> cancelOrder(
            @PathVariable Long orderId,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId, user));
    }


    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status
    ) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PutMapping("/return")
    public ResponseEntity<OrderResponseDto> returnOrder(@AuthenticationPrincipal User user , Long orderId){
        return new ResponseEntity<>(orderService.returnOrder(user , orderId) , HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/ship/{orderId}")
    public ResponseEntity<OrderResponseDto> updateShipping(@RequestBody UpdateShippingDetailsDto shippingDetailsDto) {
        return ResponseEntity.ok(orderService.updateShippingDetails(shippingDetailsDto));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/deliver/{orderId}")
    public ResponseEntity<OrderResponseDto> markDelivered(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.markAsDelivered(orderId));
    }

    @GetMapping("/track/{orderId}")
    public ResponseEntity<OrderResponseDto> trackOrder(@AuthenticationPrincipal User user ,@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId , user));
    }

}
