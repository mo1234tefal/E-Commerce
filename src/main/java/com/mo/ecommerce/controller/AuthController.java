package com.mo.ecommerce.controller;

import com.mo.ecommerce.dto.auth.AuthenticationRequest;
import com.mo.ecommerce.dto.auth.AuthenticationResponse;
import com.mo.ecommerce.dto.auth.SignupRequest;
import com.mo.ecommerce.entity.User;
import com.mo.ecommerce.repository.UserRepo;
import com.mo.ecommerce.service.AuthService;
import com.mo.ecommerce.utils.JWTUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final  AuthenticationManager authenticationManager;
    private final  UserRepo userRepo;
    private final AuthService authService;
    private final UserDetailsService userDetailsService;
    private final JWTUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, UserRepo userRepo, AuthService authService, UserDetailsService userDetailsService, JWTUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepo = userRepo;
        this.authService = authService;
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request){
        if(authService.foundByEmail(request.getEmail())){
            return new ResponseEntity<>("this email is found" , HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(authService.signUp(request) , HttpStatus.CREATED);
    }

    @PostMapping("/signin")
public ResponseEntity<?> signin(@RequestBody AuthenticationRequest request){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        }catch (BadCredentialsException e){
            return new ResponseEntity<>("failed to signin" , HttpStatus.BAD_REQUEST);
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        Optional<User> user = userRepo.findByEmail(request.getEmail());
        String token = jwtUtils.generateToken(userDetails);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        if(user.isPresent()){
            authenticationResponse.setToken(token);
            authenticationResponse.setRole(user.get().getRole().name());
            authenticationResponse.setEmail(user.get().getEmail());

        }
        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
    }
}
