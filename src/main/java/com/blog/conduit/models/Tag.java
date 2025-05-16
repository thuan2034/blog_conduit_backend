package com.blog.conduit.models;

import jakarta.persistence.*;

@Entity
@Table(name="Tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="tag_name", columnDefinition = "character varying(255)")
    private String tagName;

    //getter and setter...

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
