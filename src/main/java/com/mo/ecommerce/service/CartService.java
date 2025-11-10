package com.mo.ecommerce.service;

import com.mo.ecommerce.dto.cart.CartItemDto;
// import com.mo.ecommerce.dto.cart.CartItemRequest; // لم نعد بحاجة إليه هنا
import com.mo.ecommerce.dto.cart.CartItemRequest;
import com.mo.ecommerce.dto.cart.CartResponseDto;
import com.mo.ecommerce.entity.Cart;
import com.mo.ecommerce.entity.CartItem;
import com.mo.ecommerce.entity.Product;
import com.mo.ecommerce.entity.User;
import com.mo.ecommerce.repository.CartRepo;
import com.mo.ecommerce.repository.ProductRepo;
import com.mo.ecommerce.repository.UserRepo; // (يمكن إزالته إذا لم نستخدمه في أي مكان آخر)
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepo cartRepo;

    private final ProductRepo productRepo;


    public CartResponseDto getUserCart(User user) {
        Cart cart = cartRepo.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart(user);
            return cartRepo.save(newCart);
        });
        return maptoCartResponseDto(cart);
    }


    @Transactional
    public CartResponseDto addItemToCart(User user , CartItemRequest cartItemRequest) {

        Cart cart = cartRepo.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart(user);
            newCart.setCartItems(new ArrayList<>());
            return cartRepo.save(newCart);
        });

        Product product = productRepo.findById(cartItemRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (cart.getCartItems() == null)
            cart.setCartItems(new ArrayList<>());

        CartItem existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(cartItemRequest.getProductId()))
                .findFirst()
                .orElse(null);

        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemRequest.getQuantity());
            existingCartItem.setPrice(existingCartItem.getQuantity() * product.getPrice());
        } else {
            CartItem cartItem = CartItem.builder()
                    .product(product)
                    .cart(cart)
                    .quantity(cartItemRequest.getQuantity())
                    .price(product.getPrice() * cartItemRequest.getQuantity())
                    .build();
            cart.getCartItems().add(cartItem);
        }

        updateCartTotal(cart);
        Cart savedCart = cartRepo.save(cart);
        return maptoCartResponseDto(savedCart);
    }


    @Transactional
    public CartResponseDto removeItemFromCart(User user, Long productId) {
        Cart cart = cartRepo.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart(user);
            return cartRepo.save(newCart);
        });

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElse(null);
        if (cartItem == null) {

            return maptoCartResponseDto(cart);
        }
        cart.getCartItems().remove(cartItem);
        updateCartTotal(cart);
        Cart savedCart = cartRepo.save(cart);
        return maptoCartResponseDto(savedCart);
    }


    public List<CartItemDto> getCartItems(User user) {

        Cart cart = cartRepo.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart(user);
            return cartRepo.save(newCart);
        });
        return cart.getCartItems().stream()
                .map(this::maptoCartItemDto).collect(Collectors.toList());
    }


    public CartResponseDto updateQuantity(User user,CartItemRequest request) {

        Cart cart = cartRepo.findByUser(user).orElseThrow(() -> new RuntimeException("user is not have cart"));

        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(request.getProductId()))
                .findFirst().orElse(null);
        if (cartItem == null) {
            throw new RuntimeException("Product not found in cart");
        }
        cartItem.setQuantity(request.getQuantity());
        cartItem.setPrice(product.getPrice() * request.getQuantity());
        updateCartTotal(cart);
        Cart savedCart = cartRepo.save(cart);
        return maptoCartResponseDto(savedCart);
    }


    @Transactional
    public CartResponseDto removeAllCartItems(User user) {
        Cart cart = cartRepo.findByUser(user).orElseThrow(() -> new RuntimeException("user is not have cart"));

        cart.getCartItems().clear();
        updateCartTotal(cart);
        Cart savedCart = cartRepo.save(cart);
        return maptoCartResponseDto(savedCart);
    }


    private CartResponseDto maptoCartResponseDto(Cart cart) {
        List<CartItem> items = cart.getCartItems() != null ? cart.getCartItems() : new ArrayList<>();

        List<CartItemDto> itemDtos = items.stream()
                .map(this::maptoCartItemDto)
                .toList();

        return CartResponseDto.builder()
                .id(cart.getId())
                .username(cart.getUser().getUsername())
                .cartItems(itemDtos)
                .totalprice(items.stream()
                        .mapToDouble(CartItem::getPrice)
                        .sum())
                .build();
    }

    private CartItemDto maptoCartItemDto(CartItem cartItem) {
        return CartItemDto.builder()
                .id(cartItem.getId())
                .price(cartItem.getPrice())
                .quantity(cartItem.getQuantity())
                .productName(cartItem.getProduct().getName())
                .build();

    }

    private void updateCartTotal(Cart cart) {
        double total = cart.getCartItems().stream()
                .mapToDouble(CartItem::getPrice)
                .sum();
        cart.setTotalPrice(total);
    }
}