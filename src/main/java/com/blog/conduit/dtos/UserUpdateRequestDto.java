package com.blog.conduit.dtos;

public class UserUpdateRequestDto {
    private String email;
    private String userName;
    private String bio;
    private String image;
    private String password;

    //constructors...
    public UserUpdateRequestDto(){}

    public UserUpdateRequestDto(String email, String password, String image, String bio, String userName) {
        this.email = email;
        this.password = password;
        this.image = image;
        this.bio = bio;
        this.userName = userName;
    }
    //getters and setters...

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
