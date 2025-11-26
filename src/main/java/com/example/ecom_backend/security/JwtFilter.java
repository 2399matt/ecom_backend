package com.example.ecom_backend.security;

import com.example.ecom_backend.dto.JwtPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Profile("!test")
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    //private final ApplicationContext context;

    public JwtFilter(JwtService jwtService, ApplicationContext context) {
        this.jwtService = jwtService;
        //this.context = context;
    }

    //TODO Put id as a claim and remove this DB hit. We can just populate the security context without it, what the hell?
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            String username = jwtService.extractUsername(token);
            if (username != null) {
                //EUserDetails userDetails = (EUserDetails) context.getBean(EUserDetailsService.class).loadUserByUsername(username);
                if (jwtService.validateToken(token)) {
                    int id = jwtService.extractId(token);
                    List<SimpleGrantedAuthority> roles = jwtService.extractRoles(token).stream().map(role -> new SimpleGrantedAuthority(role)).toList();
                    JwtPrincipal principal = new JwtPrincipal(username, id, roles);
                    UsernamePasswordAuthenticationToken uap = new UsernamePasswordAuthenticationToken(principal, null, roles);
                    uap.setDetails(new WebAuthenticationDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(uap);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
