package com.blog.conduit.repositories;

import com.blog.conduit.models.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
    Optional<Article> findBySlug(String slug);
    Optional<Article> findByTitle(String title);
    Page<Article> findAll(Pageable pageable);
}
