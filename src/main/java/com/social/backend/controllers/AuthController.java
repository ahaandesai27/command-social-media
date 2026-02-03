package com.social.backend.controllers;

import com.social.backend.payloads.LoginRequest;
import com.social.backend.payloads.LoginResponse;
import com.social.backend.payloads.user.UserCreateDto;
import com.social.backend.security.userdetails.CustomUserDetails;
import com.social.backend.services.UserService;
import com.social.backend.security.jwt.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(),
                                                            request.getPassword())
            );

            CustomUserDetails userDetails =
                    (CustomUserDetails) authentication.getPrincipal();  // authentication has already loaded the user

            assert  userDetails != null;

            String token = jwtUtil.generateToken(userDetails);
            return ResponseEntity.ok(new LoginResponse(token));
        }
        catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserCreateDto userDto) {
        // Note: Angular expects JSON so return either JSON or void
        this.userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
