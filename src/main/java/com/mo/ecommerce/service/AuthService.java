package com.mo.ecommerce.service;

import com.mo.ecommerce.dto.auth.SignupRequest;
import com.mo.ecommerce.entity.User;
import com.mo.ecommerce.enums.Role;
import com.mo.ecommerce.repository.UserRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final  UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }


    public String signUp(SignupRequest signupRequest){
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setEmail(signupRequest.getEmail());
        user.setRole(Role.CUSTOMER);
        userRepo.save(user);
        return "User added successfully";
    }
    @PostConstruct
    public void AddAdmin(){
        User adminAccount = userRepo.findByRole(Role.SUPER_ADMIN);
        if(adminAccount == null){
            User user = new User();
            user.setUsername("Mohamed");
            user.setEmail("m@gmail.com");
            user.setRole(Role.SUPER_ADMIN);
            user.setPassword(passwordEncoder.encode("mohamed"));
            userRepo.save(user);
        }
    }

    public boolean foundByEmail(String email){
        return userRepo.findByEmail(email).isPresent();
    }

}
