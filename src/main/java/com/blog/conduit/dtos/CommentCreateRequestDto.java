package com.blog.conduit.dtos;

public class CommentCreateRequestDto {
    private String slug;
    private String body;


    //constructors...
    public CommentCreateRequestDto() {
    }

    public CommentCreateRequestDto(String slug, String body) {
        this.slug = slug;
        this.body = body;
    }

    //getters and setters...


    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
