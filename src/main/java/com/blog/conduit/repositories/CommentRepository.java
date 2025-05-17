package com.blog.conduit.repositories;

import com.blog.conduit.models.Article;
import com.blog.conduit.models.Comment;
import com.blog.conduit.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    public List<Comment> findByArticle(Article article);
    public List<Comment> findByAuthor(User user);
}
