package com.blog.conduit.controllers;

import com.blog.conduit.dtos.UserResponseDto;
import com.blog.conduit.services.UserService;
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
    public UserResponseDto updateUser(@RequestBody UserResponseDto updatedUser){
        return userService.updateUser(updatedUser);
    }
}
