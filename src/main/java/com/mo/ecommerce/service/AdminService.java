package com.mo.ecommerce.service;

import com.mo.ecommerce.dto.user.AdminDashboardDto;
import com.mo.ecommerce.dto.user.AdminRequest;
import com.mo.ecommerce.dto.user.UserDto;
import com.mo.ecommerce.entity.Order;
import com.mo.ecommerce.entity.User;
import com.mo.ecommerce.enums.Role;
import com.mo.ecommerce.repository.OrderRepo;
import com.mo.ecommerce.repository.ProductRepo;
import com.mo.ecommerce.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final OrderRepo orderRepo;
    private final ProductRepo productRepo;

    public UserDto addAdmin(User user , AdminRequest adminRequest){
        if(user.getRole() == Role.SUPER_ADMIN){
            User admin = new User();
            admin.setEmail(adminRequest.getEmail());
            admin.setUsername(adminRequest.getUsername());
            admin.setPassword(passwordEncoder.encode(adminRequest.getPassword()));
            admin.setRole(Role.ADMIN);
            User savedAdmin =  userRepo.save(admin);
            return UserDto.builder().role(savedAdmin.getRole().name())
                    .username(savedAdmin.getUsername()).email(savedAdmin.getEmail()).id(savedAdmin.getId()).build();
        }
        throw new RuntimeException("you do not have authority to do this");
    }

    public String deleteAdmin(User user , Integer adminId){
        User admin = userRepo.findById(adminId).orElseThrow(()-> new RuntimeException("admin is not found "));
        if(user.getRole() == Role.SUPER_ADMIN){
            userRepo.delete(admin);
            return "success";
        }
        throw new RuntimeException("you do not have authority to do this");
    }

    public List<UserDto> findAllAdmin(){
        return userRepo.findAllByRole(Role.ADMIN)
                .stream()
                .map(user->{return UserDto.builder().id(user.getId()).role(user.getRole().name()).email(user.getEmail()).username(user.getUsername()).build();

                })
                .collect(Collectors.toList());
    }
    public UserDto findAdminById(Integer id){
        User admin = userRepo.findById(id).orElseThrow(()-> new RuntimeException("admin is not found "));
        return UserDto.builder().email(admin.getEmail())
                .username(admin.getUsername())
                .role(admin.getRole().name())
                .id(admin.getId()).build();
    }

    public List<UserDto> findAllUsers(){
        return userRepo.findAllByRole(Role.CUSTOMER)
                .stream()
                .map(user->  {return UserDto.builder().role(user.getRole().name()).username(user.getUsername()).email(user.getEmail()).id(user.getId()).build();})
                .collect(Collectors.toList());
    }


    public UserDto findUserByEmail(String email){
        User user = userRepo.findByEmail(email).orElseThrow(()-> new RuntimeException("user is not found "));
        return UserDto.builder()
                .email(user.getEmail())
                .role(user.getRole().name())
                .username(user.getUsername())
                .id(user.getId())
                .build();
    }

    public String deleteUser(String email){
        User user = userRepo.findByEmail(email).orElseThrow(()-> new RuntimeException("user is not found"));
        userRepo.delete(user);
        return "user that have email " + user.getEmail() +" is deleted";
    }
    public AdminDashboardDto getDashboardStats() {
        long totalUsers = userRepo.count();
        long totalOrders = orderRepo.count();
        long totalProducts = productRepo.count();
        double totalRevenue = orderRepo.findAll()
                .stream()
                .mapToDouble(Order::getTotalPrice)
                .sum();

        return AdminDashboardDto.builder()
                .totalUsers(totalUsers)
                .totalOrders(totalOrders)
                .totalProducts(totalProducts)
                .totalRevenue(totalRevenue)
                .build();
    }
    public UserDto changeUserRole(Integer userId, Role newRole) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(newRole);
        User updatedUser = userRepo.save(user);
        return UserDto.builder()
                .username(updatedUser.getUsername())
                .email(updatedUser.getEmail())
                .role(updatedUser.getRole().name())
                .id(updatedUser.getId())
                .build();
    }


}
