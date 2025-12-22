package seniorproject.bankifycore.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import seniorproject.bankifycore.domain.User;
import seniorproject.bankifycore.repository.UserRepository;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initAdminUser(){
        return args -> {
            String adminEmail = "admin@bankify.local";
            if(userRepository.findByEmail(adminEmail).isEmpty()){
                User admin = User.builder()
                        .email(adminEmail)
                        .passwordHash(passwordEncoder.encode("admin123"))
                        .role(User.Role.ADMIN)
                        .status(User.Status.ACTIVE)
                        .build();
                userRepository.save(admin);
                System.out.println("Created default admin user:  "+adminEmail+" / admin123");
            }
        };
    }
}
