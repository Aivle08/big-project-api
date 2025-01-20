package com.aivle08.big_project_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults()) // CORS 허용
                .csrf(csrf -> csrf.disable()) // CSRF 완전히 비활성화

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/h2-console/**",
                                "/api-docs",
                                "/api/v3/**",
                                "/swagger-ui/**",
                                "/swagger-resources/**",
                                "/api/v1/users/register/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
//                .csrf(csrf -> csrf.ignoringRequestMatchers(
//                        "/api-docs",
//                        "/api/v3/**",
//                        "/swagger-ui/**",
//                        "/swagger-resources/**",
//                        "/api/v1/users/register/**",
//                        "/h2-console/**"
//                ))

                .headers(headers -> headers.frameOptions().disable());

        return http.build();
    }


}
