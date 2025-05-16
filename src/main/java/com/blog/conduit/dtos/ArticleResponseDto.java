package com.blog.conduit.dtos;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

public class ArticleResponseDto {
    private Integer id;
    private String slug;
    private String title;
    private String description;
    private String body;
    private Integer favoritesCount;
    private AuthorDto author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> tagList;

    //constructors...
    public ArticleResponseDto() {
    }

    public ArticleResponseDto(Integer id, String slug, String title, String body, String description, Integer favoritesCount, AuthorDto author, LocalDateTime updatedAt, LocalDateTime createdAt) {
        this.id = id;
        this.slug = slug;
        this.title = title;
        this.body = body;
        this.description = description;
        this.favoritesCount = favoritesCount;
        this.author = author;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    //getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getFavoritesCount() {
        return favoritesCount;
    }

    public void setFavoritesCount(Integer favoritesCount) {
        this.favoritesCount = favoritesCount;
    }

    public AuthorDto getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDto author) {
        this.author = author;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
