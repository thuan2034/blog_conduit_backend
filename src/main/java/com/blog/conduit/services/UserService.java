package com.blog.conduit.services;

import com.blog.conduit.dtos.UserCreateRequestDto;
import com.blog.conduit.dtos.UserResponseDto;
import com.blog.conduit.enums.UserRole;
import com.blog.conduit.models.User;
import com.blog.conduit.repositories.TagRepository;
import com.blog.conduit.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> findAll() {
        return userRepo.findAll().stream().map(this::mapToDto).toList();
    }

    @Transactional
    public Optional<User> findByIdEntity(Integer id) {
        // Logic tìm User entity trong repository
        return userRepo.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<UserResponseDto> findById(Integer id) {
        return userRepo.findById(id).map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public Optional<UserResponseDto> findByUserName(String userName) {
        return userRepo.findByUserName(userName).map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public Optional<UserResponseDto> findByEmail(String email) {
        return userRepo.findByEmail(email).map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public Optional<User> findUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Transactional
    public UserResponseDto create(UserCreateRequestDto userDto) {
        User newUser = new User();
        // Ánh xạ dữ liệu từ DTO sang Entity
        newUser.setUserName(userDto.getUserName());
        newUser.setEmail(userDto.getEmail());
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        newUser.setPassword_hash(encodedPassword);
        newUser.setBio(userDto.getBio());
        newUser.setImage(userDto.getImage());
        newUser.setRole(userDto.getRole() != null ? userDto.getRole() : UserRole.USER);
        userRepo.save(newUser);
        return new UserResponseDto(newUser.getUserName(), newUser.getBio(), newUser.getImage());
    }


    private UserResponseDto mapToDto(User user) {
        return new UserResponseDto(user.getUserName(), user.getBio(), user.getImage());
    }
}
