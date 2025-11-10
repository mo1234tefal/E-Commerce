package com.mo.ecommerce.controller;

import com.mo.ecommerce.dto.cart.CartItemDto;
import com.mo.ecommerce.dto.cart.CartItemRequest;
import com.mo.ecommerce.dto.cart.CartResponseDto;
import com.mo.ecommerce.entity.User;
import com.mo.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/")
    public ResponseEntity<CartResponseDto> getUserCart(@AuthenticationPrincipal User loggedInUser){
        return new ResponseEntity<>(cartService.getUserCart(loggedInUser) , HttpStatus.OK);
    }
    @PostMapping("/")
    public ResponseEntity<CartResponseDto> addItemsToCart(@RequestBody CartItemRequest request , @AuthenticationPrincipal User loggedInUser){
        return new ResponseEntity<>(cartService.addItemToCart(loggedInUser , request) , HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<CartResponseDto> removeItemFromCart(@AuthenticationPrincipal User loggedInUser , @PathVariable Long productId){
        return new ResponseEntity<>(cartService.removeItemFromCart(loggedInUser , productId) , HttpStatus.OK);
    }

    @GetMapping("/items")
    public ResponseEntity<List<CartItemDto>> getCartItems(@AuthenticationPrincipal User user){
        return new ResponseEntity<>(cartService.getCartItems(user) , HttpStatus.OK);
    }

    @PutMapping("/quantity")
    public ResponseEntity<CartResponseDto> updateQuantity(@AuthenticationPrincipal User user , @RequestBody CartItemRequest cartItemRequest){
        return new ResponseEntity<>(cartService.updateQuantity(user , cartItemRequest) , HttpStatus.OK);
    }
    @DeleteMapping("/items")
    public ResponseEntity<CartResponseDto> removeAllCartItems(@AuthenticationPrincipal User user){
        return new ResponseEntity<>(cartService.removeAllCartItems(user) , HttpStatus.OK);
    }


    


}
