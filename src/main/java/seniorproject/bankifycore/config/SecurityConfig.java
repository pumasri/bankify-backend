package seniorproject.bankifycore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import seniorproject.bankifycore.repository.ClientAppRepository;
import seniorproject.bankifycore.security.ApiKeyAuthenticationFilter;
import seniorproject.bankifycore.security.AtmJwtAuthenticationFilter;
import seniorproject.bankifycore.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import seniorproject.bankifycore.security.JwtTokenService;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final ClientAppRepository clientAppRepository;

        @Value("${security.api-key.pepper:change-me}")
        private String apiKeyPepper;

        @Bean
        @Order(1)
        public SecurityFilterChain partnerChain(HttpSecurity http) throws Exception {
                http.securityMatcher("/api/partner/**")
                                .csrf(csrf -> csrf.disable())
                                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth.anyRequest().hasRole("PARTNER"))
                                .addFilterBefore(new ApiKeyAuthenticationFilter(clientAppRepository, apiKeyPepper),
                                                UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        @Order(2)
        public SecurityFilterChain atmChain(HttpSecurity http, JwtTokenService jwtTokenService) throws Exception {
                http.securityMatcher("/api/atm/**")
                                .csrf(csrf -> csrf.disable())
                                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/api/atm/login").permitAll()
                                                .anyRequest().hasRole("ATM"))
                                .addFilterBefore(new AtmJwtAuthenticationFilter(jwtTokenService),
                                                org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        // 2) Admin/Human chain: everything else (JWT)
        @Bean
        @Order(3)
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/api/health", "/auth/login").permitAll()
                                                .anyRequest().authenticated())
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
                        throws Exception {
                return configuration.getAuthenticationManager();
        }
}
