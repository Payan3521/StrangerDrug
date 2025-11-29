package com.desarrollox.backend.core.securityConfig;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;
import org.springframework.web.filter.OncePerRequestFilter;
import com.desarrollox.backend.core.JwtSecurity.ITokenService;
import com.desarrollox.backend.core.exception.NoTokenException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter{

    private final ITokenService tokenService;

    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, 
            HttpServletResponse response, 
            FilterChain filterChain
        )throws ServletException, IOException {
        
        String path = request.getRequestURI();
        String method = request.getMethod();
        String authHeader = request.getHeader("Authorization");
        
        // Permitir acceso libre solo a endpoints específicos
        if(
            (path.equals("/stranger-drug/api/auth/login") && method.equals("POST")) ||
            (path.equals("/stranger-drug/api/models") && method.equals("GET")) ||
            (path.equals("/stranger-drug/api/models/name") && method.equals("GET")) ||
            (path.equals("/stranger-drug/api/models/salients-models") && method.equals("GET")) ||
            (path.equals("/stranger-drug/api/posts") && method.equals("GET")) ||
            (path.equals("/stranger-drug/api/posts/model-name") && method.equals("GET")) ||
            (path.equals("/stranger-drug/api/posts/section-name") && method.equals("GET")) ||
            (path.equals("/stranger-drug/api/posts/title") && method.equals("GET")) ||
            (path.equals("/stranger-drug/api/posts/recent") && method.equals("GET")) ||
            (path.equals("/stranger-drug/api/register") && method.equals("POST")) ||
            (path.equals("/stranger-drug/api/sections") && method.equals("GET"))    
        ){
            filterChain.doFilter(request, response);
            return;
        }

        if(authHeader == null || !authHeader.startsWith(BEARER_PREFIX)){
            throw new NoTokenException();
        }

        // Extraer el token y quitar el prefijo "Bearer "
        String token = authHeader.substring(BEARER_PREFIX.length());

        try{
            // Validar el token y obtener el Claims
            Claims claims = tokenService.extractAllClaims(token);
            
            if(claims != null){
                String email = claims.getSubject();
                String role = claims.get("role", String.class);

                List<GrantedAuthority> authorities = Collections.emptyList();

                if(role != null){
                    authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
                }

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    email, null, authorities
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }catch(Exception e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            Map<String, String> error = Map.of("message", "Token inválido");
            new ObjectMapper().writeValue(response.getOutputStream(), error);
            return;
        }

        filterChain.doFilter(request, response);
        
    }
    
}