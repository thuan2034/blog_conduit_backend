package com.blog.conduit.models;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ArticleFavoriteId implements Serializable {
    private Integer articleId;
    private Integer userId;

    //constructors...
    public ArticleFavoriteId(){};
    public ArticleFavoriteId(Integer articleId,Integer userId){
        this.articleId=articleId;
        this.userId=userId;
    }

    //getters...
    public Integer getArticleId() {
        return articleId;
    }

    public Integer getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o==null || getClass()!=o.getClass()) return false;
        ArticleFavoriteId that = (ArticleFavoriteId) o;
        return Objects.equals(this.articleId,that.articleId) && Objects.equals(this.userId,that.userId);
    }

    @Override
    public int hashCode(){
        return Objects.hash(articleId,userId);
    }
}
