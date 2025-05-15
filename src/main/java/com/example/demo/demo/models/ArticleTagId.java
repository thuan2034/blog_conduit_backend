package com.example.demo.demo.models;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ArticleTagId implements Serializable {
    private Integer articleId;
    private Integer tagId;

    public ArticleTagId(){};
    public ArticleTagId(Integer articleId,Integer tagId){
        this.articleId = articleId;
        this.tagId=tagId;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public Integer getTagId() {
        return tagId;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if((o==null)||getClass() != o.getClass()) return false;
        ArticleTagId that = (ArticleTagId) o;
        return Objects.equals(this.articleId,that.articleId) &&
                Objects.equals(this.tagId,that.tagId);
    }

    @Override
    public int hashCode(){
        return Objects.hash(articleId,tagId);
    }
}
