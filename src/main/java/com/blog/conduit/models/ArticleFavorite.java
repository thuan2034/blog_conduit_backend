package com.blog.conduit.models;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="article_favorite")
public class ArticleFavorite {
    @EmbeddedId
    private ArticleFavoriteId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @MapsId("articleId")
    @JoinColumn(name="article_id")
    private Article article;

    @CreatedDate
    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime createdAt;

    //constructors...
    public ArticleFavorite(){}
    public ArticleFavorite(ArticleFavoriteId id, User user, Article article, OffsetDateTime createdAt) {
        this.user = user;
        this.article = article;
        this.createdAt = createdAt;
        this.id =new ArticleFavoriteId(article.getId(), user.getId());
    }

    //getters and setters...

    public ArticleFavoriteId getId() {
        return id;
    }

    public void setId(ArticleFavoriteId id) {
        this.id = id;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
