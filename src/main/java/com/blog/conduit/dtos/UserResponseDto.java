package com.blog.conduit.dtos;

public class UserResponseDto {
    private String userName;
    private String bio;
    private String image;

    public UserResponseDto() {
    }

    public UserResponseDto(String userName, String bio, String image) {
        this.userName = userName;
        this.bio = bio;
        this.image = image;
    }

    //getters and setters...
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
}
