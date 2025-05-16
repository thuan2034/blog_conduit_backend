package com.blog.conduit.repositories;

import com.blog.conduit.models.Article;
import com.blog.conduit.models.ArticleTag;
import com.blog.conduit.models.ArticleTagId;
import com.blog.conduit.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleTagRepository extends JpaRepository<ArticleTag, ArticleTagId> {
    List<ArticleTag> findByTag(Tag tag);
    List<ArticleTag> findByArticle(Article article);
}
