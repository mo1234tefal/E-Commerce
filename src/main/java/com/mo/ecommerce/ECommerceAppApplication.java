package com.mo.ecommerce;

import com.mo.ecommerce.entity.User;
import com.mo.ecommerce.enums.Role;
import com.mo.ecommerce.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@RequiredArgsConstructor
public class ECommerceAppApplication {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    public static void main(String[] args) {
        SpringApplication.run(ECommerceAppApplication.class, args);
    }
    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {
            User adminAccount = userRepo.findByRole(Role.SUPER_ADMIN);

            if (adminAccount == null) {
                User user = new User();
                user.setUsername("Mohamed");
                user.setEmail("m@gmail.com");
                user.setRole(Role.SUPER_ADMIN);
                user.setPassword(passwordEncoder.encode("mohamed"));
                userRepo.save(user);
                System.out.println(">>> Initial SUPER_ADMIN account created successfully.");
            }
        };

}
}
