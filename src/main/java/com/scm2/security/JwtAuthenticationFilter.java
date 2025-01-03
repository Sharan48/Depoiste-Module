package com.scm2.security;

import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // get token from request
        String token = getTokenFormRequest(request);
        // System.out.println("token :" + token);
        // System.out.println(jwtTokenProvider.getUserName(token));

        // validate token
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            // get username from token
            String username = jwtTokenProvider.getUsername(token);
            // System.out.println(username);
            // load the user associate with token
            UserDetails userdetails = userDetailsService.loadUserByUsername(username);
            // System.out.println(userdetails);
            UsernamePasswordAuthenticationToken authenticateToken = new UsernamePasswordAuthenticationToken(userdetails,
                    null, userdetails.getAuthorities());
            authenticateToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticateToken);
        }
        filterChain.doFilter(request, response);

    }

    private String getTokenFormRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        // System.out.println("bearerToken :" + bearerToken);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7, bearerToken.length());

        }
        return null;
    }

}