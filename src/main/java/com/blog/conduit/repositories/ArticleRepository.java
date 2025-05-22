package com.blog.conduit.repositories;

import com.blog.conduit.models.Article;
import com.blog.conduit.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
    Optional<Article> findBySlug(String slug);
    Optional<Article> findByTitle(String title);
    Page<Article> findAll(Pageable pageable);

    @Query("SELECT a FROM Article a " +
            "WHERE a.author.id IN (" + // Lọc bài viết mà tác giả (a.author) có ID nằm trong danh sách...
            "SELECT f.followedUser.id FROM Follow f " + // ...các user được follow (f.followedUser)
            "WHERE f.followingUser = :currentUser" + // ...bởi người dùng hiện tại (:currentUser)
            ")")
    Page<Article> findFeedArticles(@Param("currentUser") User currentUser, Pageable pageable);
    boolean existsBySlug(String slug);
    boolean existsByTitle(String title);
}
