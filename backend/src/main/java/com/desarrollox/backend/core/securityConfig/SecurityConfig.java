package com.desarrollox.backend.core.securityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

                // Auth
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/refresh-token").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/auth/logout").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/auth/check-token").authenticated()

                //models
                .requestMatchers(HttpMethod.GET, "/api/models").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/models/name").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/models/salients-models").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/models").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/models/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/models/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/models/{id}").hasRole("ADMIN")

                //notifications
                .requestMatchers(HttpMethod.POST, "/api/notifications").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/notifications/{receiveId}").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/notifications/{id}").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/notifications/clear/{receiveId}").authenticated()

                //payment
                .requestMatchers(HttpMethod.GET, "/api/payment/initiate").hasRole("CLIENTE")
                .requestMatchers(HttpMethod.GET, "/api/payment/postback").hasRole("CLIENTE")

                //posts
                .requestMatchers(HttpMethod.GET, "/api/posts").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/posts/model-name").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/posts/section-name").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/posts/title").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/posts/recent").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/posts/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/posts/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/posts/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/posts").hasRole("ADMIN")

                //purchases
                .requestMatchers(HttpMethod.GET, "/api/purchases/buyerUser/{buyerUserId}").hasRole("CLIENTE")
                .requestMatchers(HttpMethod.POST, "/api/purchases").hasRole("CLIENTE")
                .requestMatchers(HttpMethod.DELETE, "/api/purchases/soft-delete-cliente/{id}").hasRole("CLIENTE")
                .requestMatchers(HttpMethod.DELETE, "/api/purchases/soft-delete-admin/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/purchases/soft-delete-clear-admin").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/purchases/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/purchases").hasRole("ADMIN")

                //register
                .requestMatchers(HttpMethod.POST, "/api/register").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/register/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/register/email").hasRole("ADMIN")
                
                //sections
                .requestMatchers(HttpMethod.GET, "/api/sections").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/sections/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/sections").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/sections/{id}").hasRole("ADMIN")

                //photos
                .requestMatchers(HttpMethod.POST, "/api/photos/upload-thumbnail").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/photos/upload-profile").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/photos/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/photos/update-thumbnail/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/photos/update-profile/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/photos/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/photos").hasRole("ADMIN")

                //videos
                .requestMatchers(HttpMethod.POST, "/api/videos/upload-video").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/videos/upload-preview").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/videos").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/videos/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/videos/update-preview/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/videos/update-video/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/videos/{id}").hasRole("ADMIN")

                .anyRequest().authenticated()
                
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
            
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}