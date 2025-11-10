package com.mo.ecommerce.controller;

import com.mo.ecommerce.dto.wishlist.WishlistDto;
import com.mo.ecommerce.entity.User;
import com.mo.ecommerce.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/add/{productId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<WishlistDto> addToWishlist(
            @AuthenticationPrincipal User user,
            @PathVariable Long productId) {
        return ResponseEntity.ok(wishlistService.addToWishlist(user ,productId));
    }

    @DeleteMapping("/remove/{productId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<WishlistDto> removeFromWishlist(
            @AuthenticationPrincipal User user,
            @PathVariable Long productId) {
        return ResponseEntity.ok(wishlistService.removeFromWishlist(user, productId));
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<WishlistDto> getWishlist(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(wishlistService.getUserWishlist(user));
    }
}

