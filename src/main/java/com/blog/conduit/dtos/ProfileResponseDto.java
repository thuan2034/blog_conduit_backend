package com.blog.conduit.dtos;

public class ProfileResponseDto {
    private String userName;
    private String bio;
    private String image;
    private Boolean following;

    public ProfileResponseDto() {
    }

    public ProfileResponseDto(String userName, String bio, String image) {
        this.userName = userName;
        this.bio = bio;
        this.image = image;
        this.following = false;
    }

    public ProfileResponseDto(String userName, String bio, String image, boolean following) {
        this.userName = userName;
        this.bio = bio;
        this.image = image;
        this.following = following ;
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

    public Boolean getFollowing() {
        return following;
    }

    public void setFollowing(Boolean following) {
        this.following = following;
    }
}
