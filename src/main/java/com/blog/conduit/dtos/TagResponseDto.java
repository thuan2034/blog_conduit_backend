package com.blog.conduit.dtos;

public class TagResponseDto {
    private String name;

    public TagResponseDto(){}
    public TagResponseDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
