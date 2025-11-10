package com.mo.ecommerce.dto.auth;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private String token;
    private String email;
    private String role;
}
