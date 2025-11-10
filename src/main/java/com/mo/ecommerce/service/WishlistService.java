package com.mo.ecommerce.service;


import com.mo.ecommerce.dto.product.ProductResponse;
import com.mo.ecommerce.dto.wishlist.WishlistDto;
import com.mo.ecommerce.entity.Product;
import com.mo.ecommerce.entity.User;
import com.mo.ecommerce.entity.Wishlist;
import com.mo.ecommerce.exception.ResourceNotFoundException;
import com.mo.ecommerce.repository.ProductRepo;
import com.mo.ecommerce.repository.UserRepo;
import com.mo.ecommerce.repository.WishlistRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WishlistService {

    private final WishlistRepo wishlistRepo;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;

    public WishlistDto addToWishlist(User user, Long productId) {

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Wishlist wishlist = wishlistRepo.findByUser(user)
                .orElseGet(() -> {
                    Wishlist newWishlist = new Wishlist();
                    newWishlist.setUser(user);
                    return wishlistRepo.save(newWishlist);
                });

        if (!wishlist.getProducts().contains(product)) {
            wishlist.getProducts().add(product);
        }

        Wishlist saved = wishlistRepo.save(wishlist);
        return convertToDto(saved);
    }

    public WishlistDto removeFromWishlist(User user, Long productId) {
        Wishlist wishlist = wishlistRepo.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found"));

        wishlist.getProducts().removeIf(p -> false);
        Wishlist updated = wishlistRepo.save(wishlist);

        return convertToDto(updated);
    }

    public WishlistDto getUserWishlist(User user) {

        Wishlist wishlist = wishlistRepo.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found"));

        return convertToDto(wishlist);
    }

    private WishlistDto convertToDto(Wishlist wishlist) {
        List<ProductResponse> products = wishlist.getProducts().stream()
                .map(product -> ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .imageUrl(product.getImageUrl())
                        .build())
                .collect(Collectors.toList());

        return WishlistDto.builder()
                .id(wishlist.getId())
                .userId(wishlist.getUser().getId())
                .products(products)
                .build();
    }
}

