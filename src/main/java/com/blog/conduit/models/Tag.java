package com.blog.conduit.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tag_name", columnDefinition = "character varying(255)")
    private String tagName;

    //constructors...
    public Tag() {
    }

    public Tag(String tagName) {
        this.tagName = tagName;
    }

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ArticleTag> articles = new HashSet<>();


    //getter and setter...

    public Set<ArticleTag> getArticles() {
        return articles;
    }

    public void setArticles(Set<ArticleTag> articles) {
        this.articles = articles;
    }

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
