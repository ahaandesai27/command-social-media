package com.social.backend.security.jwt;

import com.social.backend.security.userdetails.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


// once per request filter is a spring security base class that is used to implement a filter
// that is guaranteed to execute only once per HTTP request
// "middleware" of sorts

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // It is the method executed for all incoming HTTP requests
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;


        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(token);
            } catch (Exception e) {
                // Token is invalid
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // second part is checking if auth does not exist
            // only then we do auth

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null,
                                userDetails.getAuthorities());
                        // creates authentication object

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));       // attaches request details IP addr, session ID etc
                SecurityContextHolder.getContext().setAuthentication(authToken);                        // stores auth in security context
            }
        }

        filterChain.doFilter(request, response);        // passes control to next filter in chain - basically next() in middleware
    }
}
