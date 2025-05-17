package com.blog.conduit.controllers;

import com.blog.conduit.dtos.UserCreateRequestDto;
import com.blog.conduit.dtos.UserResponseDto;
import com.blog.conduit.models.ResponseObject;
import com.blog.conduit.models.User;
import com.blog.conduit.repositories.UserRepository;
import com.blog.conduit.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/users")
public class UserController {
    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponseDto> getAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> findById(@PathVariable Integer id) {
        Optional<UserResponseDto> foundUser = userService.findById(id);
        return foundUser.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "found user", foundUser)) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("false", "cannot find user with id" + id, ""));
    }

    @PostMapping
    public ResponseEntity<ResponseObject> createUser(@RequestBody UserCreateRequestDto userDto) {
        Optional<UserResponseDto> foundUsername = userService.findByUserName(userDto.getUserName());
        Optional<UserResponseDto> foundEmail = userService.findByEmail(userDto.getEmail());
        if (foundUsername.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ResponseObject("false", "username: " + userDto.getUserName() + " already taken", ""));
        if (foundEmail.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ResponseObject("false", "email: " + userDto.getEmail() + " already exist", ""));
        UserResponseDto createdUser = userService.create(userDto);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "created user", createdUser));
    }
}
