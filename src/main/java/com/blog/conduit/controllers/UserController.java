package com.blog.conduit.controllers;

import com.blog.conduit.dtos.UserCreateRequestDto;
import com.blog.conduit.dtos.UserResponseDto;
import com.blog.conduit.dtos.UserUpdateRequestDto;
import com.blog.conduit.models.ResponseObject;
import com.blog.conduit.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public UserResponseDto getCurrentUser(){
        return userService.getCurrentUser();
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody UserCreateRequestDto updatedUser){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK","updated user info",userService.updateUser(updatedUser)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseObject("false", e.getMessage(), ""));
        }
    }
}
