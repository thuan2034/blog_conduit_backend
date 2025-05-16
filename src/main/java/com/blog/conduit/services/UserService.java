package com.blog.conduit.services;

import com.blog.conduit.dtos.UserCreateRequestDto;
import com.blog.conduit.enums.UserRole;
import com.blog.conduit.models.User;
import com.blog.conduit.repositories.TagRepository;
import com.blog.conduit.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Integer id) {
        return userRepo.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUserName(String userName) {
        return userRepo.findByUserName(userName);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Transactional
    public User create(UserCreateRequestDto userDto){
        User newUser = new User();
        // Ánh xạ dữ liệu từ DTO sang Entity
        newUser.setUserName(userDto.getUserName());
        newUser.setEmail(userDto.getEmail());
        newUser.setPassword_hash(userDto.getPassword());
        newUser.setBio(userDto.getBio());
        newUser.setImage(userDto.getImage());
        newUser.setRole(userDto.getRole() != null ? userDto.getRole() : UserRole.USER);
        return userRepo.save(newUser);
    }


}
