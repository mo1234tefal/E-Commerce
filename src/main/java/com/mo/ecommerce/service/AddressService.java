package com.mo.ecommerce.service;

import com.mo.ecommerce.dto.address.AddressRequest;
import com.mo.ecommerce.dto.address.AddressResponse;
import com.mo.ecommerce.entity.Address;
import com.mo.ecommerce.entity.User;
import com.mo.ecommerce.repository.AddressRepo;
import com.mo.ecommerce.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepo addressRepo;
    private final UserRepo userRepo;

    public AddressResponse addAddress(User user, AddressRequest request) {
        Address saved = addressRepo.save(mapToAddress(user, request));
        return mapToAddressResponse(saved);
    }

    public void removeAddress(User user, Long addressId) {
        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        if (!address.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access");
        }
        addressRepo.delete(address);
    }

    public List<AddressResponse> deleteAllUserAddresses(User user) {
        List<Address> addresses = addressRepo.findByUser(user);
        addressRepo.deleteAll(addresses);
        return addresses.stream().map(this::mapToAddressResponse).toList();
    }

    public List<AddressResponse> getAllUserAddresses(User user) {
        return addressRepo.findByUser(user)
                .stream()
                .map(this::mapToAddressResponse)
                .toList();
    }

    public AddressResponse updateAddress(User user, AddressRequest request, Long addressId) {
        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access");
        }

        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setCountry(request.getCountry());
        address.setFullName(request.getFullName());
        address.setPhoneNumber(request.getPhoneNumber());
        return mapToAddressResponse(addressRepo.save(address));
    }

    private AddressResponse mapToAddressResponse(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .fullName(address.getFullName())
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .country(address.getCountry())
                .phoneNumber(address.getPhoneNumber())
                .build();
    }

    private Address mapToAddress(User user, AddressRequest request) {
        return Address.builder()
                .fullName(request.getFullName())
                .street(request.getStreet())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .phoneNumber(request.getPhoneNumber())
                .user(user)
                .build();
    }
}

