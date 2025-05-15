package com.example.demo.demo.models;

import jakarta.persistence.*;

@Entity
@Table(name="article_tag")
public class ArticleTag {
    @EmbeddedId
    private ArticleTagId id;

    @ManyToOne
    @MapsId("articleId")
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne
    @MapsId("tagId")
    @JoinColumn(name = "tag_id")
    private Tag tag;

    // Constructors
    public ArticleTag() {}

    public ArticleTag(Article article, Tag tag) {
        this.article = article;
        this.tag = tag;
        this.id = new ArticleTagId(article.getId(), tag.getId()); // Assuming Article and Tag have getId()
    }

    // Getters and Setters
    public ArticleTagId getId() {
        return id;
    }

    public void setId(ArticleTagId id) {
        this.id = id;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
