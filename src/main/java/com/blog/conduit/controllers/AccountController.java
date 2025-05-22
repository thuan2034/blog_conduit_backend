package com.blog.conduit.controllers;

import com.blog.conduit.dtos.LoginRequestDto;
import com.blog.conduit.dtos.LoginResponseDto;
import com.blog.conduit.dtos.ProfileResponseDto;
import com.blog.conduit.dtos.UserCreateRequestDto;
import com.blog.conduit.models.ResponseObject;
import com.blog.conduit.services.AuthenticationService;
import com.blog.conduit.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("api/users")
public class AccountController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    public AccountController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity<ResponseObject> createUser(@RequestBody UserCreateRequestDto userDto) {
        try {
            ProfileResponseDto createdUser = userService.create(userDto);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "created user", createdUser));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ResponseObject("False", e.getMessage(), ""));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseObject> login(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            LoginResponseDto responseDto = authenticationService.login(loginRequestDto);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("OK", "Login successful", responseDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseObject("false", e.getMessage(), ""));
        }
    }
}
