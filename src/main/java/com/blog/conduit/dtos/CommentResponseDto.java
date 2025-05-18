package com.blog.conduit.dtos;

import java.time.LocalDateTime;

public class CommentResponseDto {
    private Integer id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String body;
    private ProfileResponseDto author;

    //constructors...
    public CommentResponseDto() {
    }

    public CommentResponseDto(Integer id, ProfileResponseDto author, String body, LocalDateTime updatedAt, LocalDateTime createdAt) {
        this.id = id;
        this.author = author;
        this.body = body;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public CommentResponseDto(Integer id, String body, LocalDateTime updatedAt, LocalDateTime createdAt) {
        this.id = id;
        this.body = body;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    //getters and setters...

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ProfileResponseDto getAuthor() {
        return author;
    }

    public void setAuthor(ProfileResponseDto author) {
        this.author = author;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
