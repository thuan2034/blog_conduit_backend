package com.blog.conduit.repositories;

import com.blog.conduit.models.Article;
import com.blog.conduit.models.ArticleFavorite;
import com.blog.conduit.models.ArticleFavoriteId;
import com.blog.conduit.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleFavoriteRepository extends JpaRepository<ArticleFavorite, ArticleFavoriteId> {
    Optional<ArticleFavorite> findByUserAndArticle(User user, Article article);
}
