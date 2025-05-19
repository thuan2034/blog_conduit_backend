package com.blog.conduit.controllers;

import com.blog.conduit.dtos.ProfileResponseDto;
import com.blog.conduit.models.ResponseObject;
import com.blog.conduit.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/profiles")
public class ProfileController {
    @Autowired
    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

//    @GetMapping
//    public List<ProfileResponseDto> getAll() {
//        return userService.findAll();
//    }

//    @GetMapping("/{id}")
//    public ResponseEntity<ResponseObject> findById(@PathVariable Integer id) {
//        Optional<ProfileResponseDto> foundUser = userService.findById(id);
//        return foundUser.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "found user", foundUser)) :
//                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("false", "cannot find user with id" + id, ""));
//    }

    @GetMapping("/{username}")
    public  ResponseEntity<ResponseObject> findByUserName(@PathVariable("username") String userName){
        Optional<ProfileResponseDto> foundProfile = Optional.ofNullable(userService.findByUserName(userName));
        return foundProfile.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "found user", foundProfile)) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("false", "cannot find user with username :"+userName, ""));

    }

    @PostMapping("/{username}/follow")
    public ResponseEntity<?> followUser(@PathVariable("username") String userName){
        Optional<ProfileResponseDto> foundProfile = Optional.ofNullable(userService.findByUserName(userName));
        return foundProfile.isPresent()?
                ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK","followed user",userService.followUser(userName))):
                ResponseEntity.status((HttpStatus.NOT_IMPLEMENTED)).body(new ResponseObject("FASLE","username not found",""));
    }

    @DeleteMapping("/{username}/follow")
    public ResponseEntity<?> unFollowUser(@PathVariable("username") String userName){
        Optional<ProfileResponseDto> foundProfile = Optional.ofNullable(userService.findByUserName(userName));
        return foundProfile.isPresent()?
                ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK","unfollowed user",userService.unFollowUser(userName))):
                ResponseEntity.status((HttpStatus.NOT_IMPLEMENTED)).body(new ResponseObject("FASLE","username not found",""));

    }
}
