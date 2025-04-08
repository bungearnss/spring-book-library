package com.learning.spring_boot_library.configs;

import com.okta.spring.boot.oauth.Okta;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/api/**") // Optional: restrict filter chain to /api/** if desired
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/books/secure/**",
                                "/api/reviews/secure/**",
                                "/api/messages/secure/**",
                                "/api/admin/secure/**")
                        .authenticated()
                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> {
                                    // Optional: customize the JwtAuthenticationConverter here
                                })
                )
                .cors(AbstractHttpConfigurer::disable) // Optional: disable CORS if needed or configure globally
                .csrf(AbstractHttpConfigurer::disable);

        http.setSharedObject(org.springframework.web.accept.ContentNegotiationStrategy.class,
                new org.springframework.web.accept.HeaderContentNegotiationStrategy());

        Okta.configureResourceServer401ResponseBody(http);

        return http.build();
    }
}

