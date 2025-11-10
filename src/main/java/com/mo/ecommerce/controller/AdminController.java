package com.mo.ecommerce.controller;

import com.mo.ecommerce.dto.user.AdminDashboardDto;
import com.mo.ecommerce.dto.user.AdminRequest;
import com.mo.ecommerce.dto.user.UserDto;
import com.mo.ecommerce.entity.User;
import com.mo.ecommerce.enums.Role;
import com.mo.ecommerce.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    @PostMapping
    public ResponseEntity<UserDto> addAdmin(@RequestBody AdminRequest request , @AuthenticationPrincipal User user){
        return new ResponseEntity<>(adminService.addAdmin(user  , request) , HttpStatus.CREATED);
    }

    @DeleteMapping("/{adminId}")
    public ResponseEntity<String> deleteAdmin(@AuthenticationPrincipal User user , @PathVariable("adminId") Integer adminId){
        return new ResponseEntity<>(adminService.deleteAdmin(user , adminId), HttpStatus.OK);
    }

    @GetMapping("/{adminId}")
    public ResponseEntity<UserDto> getAdmin(@PathVariable Integer adminId){
        return new ResponseEntity<>(adminService.findAdminById(adminId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllAdmins(){
        return new ResponseEntity<>(adminService.findAllAdmin() , HttpStatus.OK);
    }


    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        return new ResponseEntity<>(adminService.findAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/users/{userEmail}")
    public ResponseEntity<UserDto> finUser(@PathVariable String userEmail){
        return new ResponseEntity<>(adminService.findUserByEmail(userEmail) , HttpStatus.OK);
    }

    @DeleteMapping("/users/{userEmail}")
    public ResponseEntity<String> deleteUser(@PathVariable String userEmail){
        return new ResponseEntity<>(adminService.deleteUser(userEmail) , HttpStatus.OK);
    }

    @PutMapping("/user/{userId}/{role}")
    public ResponseEntity<UserDto> changeUserRole(@PathVariable Integer userId, @PathVariable Role role){
        return new ResponseEntity<>(adminService.changeUserRole(userId , role) , HttpStatus.OK);
    }

    @GetMapping("/dashboard-status")
    public ResponseEntity<AdminDashboardDto> getDashboardStatus(){
        return new ResponseEntity<>(adminService.getDashboardStats() , HttpStatus.OK);
    }
}
