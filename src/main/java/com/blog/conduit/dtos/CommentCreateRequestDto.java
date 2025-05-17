package com.blog.conduit.dtos;

public class CommentCreateRequestDto {
    private String slug;
    private String body;
    private Integer authorId;

    //constructors...
    public CommentCreateRequestDto() {
    }

    public CommentCreateRequestDto(String slug, String body, Integer authorId) {
        this.slug = slug;
        this.body = body;
        this.authorId = authorId;
    }

    //getters and setters...

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

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
