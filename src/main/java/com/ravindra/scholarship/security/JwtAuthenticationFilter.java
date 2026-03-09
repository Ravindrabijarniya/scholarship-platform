package com.ravindra.scholarship.security;

import com.ravindra.scholarship.repository.BlacklistedTokenRepository;
import com.ravindra.scholarship.service.TokenBlacklistService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService userDetailsService;
//    private final BlacklistedTokenRepository blacklistRepository;
    private final TokenBlacklistService tokenBlacklistService;

    public JwtAuthenticationFilter(JwtProvider jwtProvider,
                                   CustomUserDetailsService service ,  TokenBlacklistService tokenBlacklistService) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = service;
//        this.blacklistRepository = blacklistRepository;
        this.tokenBlacklistService = tokenBlacklistService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();



        if(path.equals("/auth/login") || path.equals("/auth/refresh")){
            filterChain.doFilter(request,response);
            return;
        }

        String header = request.getHeader("Authorization");



        if(header != null && header.startsWith("Bearer ")) {

            String token = header.substring(7);

            if(tokenBlacklistService.isBlacklisted(token)){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            String email = jwtProvider.getEmailFromToken(token);

            var userDetails = userDetailsService.loadUserByUsername(email);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request,response);
    }
}