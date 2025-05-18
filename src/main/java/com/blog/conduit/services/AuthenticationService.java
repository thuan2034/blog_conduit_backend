package com.blog.conduit.services;

import com.blog.conduit.dtos.LoginRequestDto;
import com.blog.conduit.dtos.LoginResponseDto;
import com.blog.conduit.models.User;
import com.blog.conduit.repositories.UserRepository;
import com.blog.conduit.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    public AuthenticationService(UserRepository userRepository, JwtUtil jwtUtil, 
                                AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    public LoginResponseDto login(LoginRequestDto loginRequest) {
        try {
            // Authenticate the user
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            
            // If authentication is successful, find the user
            Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
            if (!userOptional.isPresent()) {
                throw new UsernameNotFoundException("User not found with email: " + loginRequest.getEmail());
            }
            
            User user = userOptional.get();
            
            // Generate JWT token
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
            String token = jwtUtil.generateToken(userDetails);
            
            // Create and return response
            LoginResponseDto response = new LoginResponseDto();
            response.setToken(token);
            response.setEmail(user.getEmail());
            response.setUsername(user.getUserName());
            response.setBio(user.getBio());
            response.setImage(user.getImage());
            
            return response;
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect username or password");
        }
    }
} 