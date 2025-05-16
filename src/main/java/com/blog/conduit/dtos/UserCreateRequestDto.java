package com.blog.conduit.dtos; // Tạo một package mới cho DTOs

import com.blog.conduit.enums.UserRole;
// Không import các annotation JPA như @Entity, @Id, etc.
// Không import @CreatedDate

public class UserCreateRequestDto {
    private String userName;
    private String email;
    private String password; // Client gửi mật khẩu rõ, bạn sẽ hash sau
    private String bio;
    private String image;
    private UserRole role; // Hoặc bỏ qua nếu role luôn mặc định là USER

    // Getters và Setters cho các trường trên
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    // ... các getter/setter khác ...

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
}