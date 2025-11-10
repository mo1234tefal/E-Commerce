package com.mo.ecommerce.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {
    @NotBlank
    @Size(min = 4 , max = 20)
    private String username;
    @NotBlank
    @Size(min = 10 , max = 30)
    private String password;
    @NotBlank
    @Email(message = "must match email format")
    private String email;
}
