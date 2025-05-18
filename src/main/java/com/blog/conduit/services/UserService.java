package com.blog.conduit.services;

import com.blog.conduit.dtos.ProfileResponseDto;
import com.blog.conduit.dtos.UserCreateRequestDto;
import com.blog.conduit.dtos.UserResponseDto;
import com.blog.conduit.enums.UserRole;
import com.blog.conduit.models.Follow;
import com.blog.conduit.models.User;
import com.blog.conduit.repositories.FollowRepository;
import com.blog.conduit.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private final UserRepository userRepo;
    private final FollowRepository followRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepo, FollowRepository followRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.followRepo = followRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<ProfileResponseDto> findAll() {
        return userRepo.findAll().stream().map(this::mapToDto).toList();
    }

    @Transactional
    public Optional<User> findByIdEntity(Integer id) {
        // Logic tìm User entity trong repository
        return userRepo.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<ProfileResponseDto> findById(Integer id) {
        return userRepo.findById(id).map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public ProfileResponseDto findByUserName(String userName) {
        User foundUser = userRepo.findByUserName(userName).orElseThrow(() -> new EntityNotFoundException(
                "username" + userName + " không tồn tại"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();// Lấy email từ JWT subject
        if (email != null && !email.equals("anonymousUser")) {
            User currentUser = userRepo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException(
                    "User email = " + email + " không tồn tại"));
            Optional<Follow> isFollow = followRepo.findByFollowingUserAndFollowedUser(currentUser, foundUser);
            return isFollow.isPresent() ?
                    new ProfileResponseDto(foundUser.getUserName(), foundUser.getBio(), foundUser.getImage(), true) :
                    new ProfileResponseDto(foundUser.getUserName(), foundUser.getBio(), foundUser.getImage(), false);
        } else
            return new ProfileResponseDto(foundUser.getUserName(), foundUser.getBio(), foundUser.getImage());
    }

    @Transactional(readOnly = true)
    public Optional<ProfileResponseDto> findByEmail(String email) {
        return userRepo.findByEmail(email).map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public Optional<User> findUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Transactional
    public ProfileResponseDto create(UserCreateRequestDto userDto) {
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
        return new ProfileResponseDto(newUser.getUserName(), newUser.getBio(), newUser.getImage());
    }

    private ProfileResponseDto mapToDto(User user) {
        return new ProfileResponseDto(user.getUserName(), user.getBio(), user.getImage());
    }

    @Transactional
    public ProfileResponseDto followUser(String userName) {
        User followedUser = userRepo.findByUserName(userName).orElseThrow(() -> new EntityNotFoundException(
                "username" + userName + " không tồn tại"));
        ProfileResponseDto profileResponseDto = new ProfileResponseDto(followedUser.getUserName(), followedUser.getBio(), followedUser.getImage());
        profileResponseDto.setFollowing(true);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Lấy email từ JWT subject
        User followingUser = userRepo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException(
                "User email = " + email + " không tồn tại"));
        followRepo.save(new Follow(followedUser, followingUser));
        return profileResponseDto;
    }

    @Transactional
    public ProfileResponseDto unFollowUser(String userName) {
        User followedUser = userRepo.findByUserName(userName).orElseThrow(() -> new EntityNotFoundException(
                "username" + userName + " không tồn tại"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Lấy email từ JWT subject
        User followingUser = userRepo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException(
                "User email = " + email + " không tồn tại"));
        Follow foundFollow = followRepo.findByFollowingUserAndFollowedUser(followingUser, followedUser).orElseThrow(() -> new EntityNotFoundException("chưa follow nhau mà :<<"));
        followRepo.delete(foundFollow);
        return new ProfileResponseDto(followedUser.getUserName(), followedUser.getBio(), followedUser.getImage());
    }

    @Transactional
    public UserResponseDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Lấy email từ JWT subject
        User foundUser = userRepo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException(
                "User email = " + email + " không tồn tại"));
        return new UserResponseDto(foundUser.getEmail(), foundUser.getImage(), foundUser.getBio(), foundUser.getUserName());
    }

    @Transactional
    public UserResponseDto updateUser(UserResponseDto updatedUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Lấy email từ JWT subject
        User foundUser = userRepo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException(
                "User email = " + email + " không tồn tại"));
        foundUser.setUserName(updatedUser.getUserName());
        foundUser.setBio(updatedUser.getBio());
        foundUser.setImage(updatedUser.getImage());
        userRepo.save(foundUser);
        return updatedUser;
    }
}
