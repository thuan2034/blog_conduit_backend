package com.blog.conduit.repositories;

import com.blog.conduit.models.ArticleFavorite;
import com.blog.conduit.models.ArticleFavoriteId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleFavoriteRepository extends JpaRepository<ArticleFavorite, ArticleFavoriteId> {
}
