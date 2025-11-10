package com.mo.ecommerce.controller;

import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.mo.ecommerce.dto.address.AddressRequest;
import com.mo.ecommerce.dto.address.AddressResponse;
import com.mo.ecommerce.entity.User;
import com.mo.ecommerce.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;


    @PostMapping
    public ResponseEntity<AddressResponse> addAddress(
            @AuthenticationPrincipal User user,
            @Valid  @RequestBody AddressRequest addressRequest) {

        AddressResponse response = addressService.addAddress(user, addressRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<AddressResponse>> getAllAddresses(
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(addressService.getAllUserAddresses(user));
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressResponse> updateAddress(
            @AuthenticationPrincipal User user,
            @PathVariable Long addressId,
            @Valid @RequestBody AddressRequest addressRequest) {

        AddressResponse response = addressService.updateAddress(user, addressRequest, addressId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<String> removeAddress(
            @AuthenticationPrincipal User user,
            @PathVariable Long addressId) {

        addressService.removeAddress(user, addressId);
        return ResponseEntity.ok("Address deleted successfully");
    }

    @DeleteMapping("/all")
    public ResponseEntity<String> deleteAllAddresses(
            @AuthenticationPrincipal User user) {

        addressService.deleteAllUserAddresses(user);
        return ResponseEntity.ok("All addresses deleted successfully");
    }
}

