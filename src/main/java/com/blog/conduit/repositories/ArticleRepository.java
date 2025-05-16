package com.blog.conduit.repositories;

import com.blog.conduit.models.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
    Optional<Article> findBySlug(String slug);
    List<Article> findByTitle(String title);
}
