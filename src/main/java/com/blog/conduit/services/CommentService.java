package com.blog.conduit.services;

import com.blog.conduit.dtos.ProfileResponseDto;
import com.blog.conduit.dtos.CommentCreateRequestDto;
import com.blog.conduit.dtos.CommentResponseDto;
import com.blog.conduit.models.Article;
import com.blog.conduit.models.Comment;
import com.blog.conduit.models.User;
import com.blog.conduit.repositories.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private final CommentRepository commentRepo;
    private final ArticleService articleService;
    private final UserService userService;
    public CommentService(CommentRepository commentRepo, ArticleService articleService, UserService userService) {
        this.commentRepo = commentRepo;
        this.articleService = articleService;
        this.userService = userService;
    }

    @Transactional
    public List<CommentResponseDto> findByArticleSlug(String slug){
        Article foundArticle = articleService.findEntityBySlug(slug).orElseThrow(() -> new EntityNotFoundException(
                "Article slug=" + slug + " không tồn tại"));
        List<Comment> commentList = commentRepo.findByArticle(foundArticle);
        return commentList.stream().map(this::mapToDto).toList();
    }

    private CommentResponseDto mapToDto(Comment comment){
        User author=comment.getAuthor();
        ProfileResponseDto profileResponseDto = new ProfileResponseDto(author.getUserName(), author.getBio(), author.getImage());
        return new CommentResponseDto(comment.getId(), profileResponseDto, comment.getBody(), comment.getUpdatedAt(), comment.getCreatedAt());
    }

    @Transactional
    public CommentResponseDto create(CommentCreateRequestDto commentCreateRequestDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Lấy email từ JWT subject
        Article foundArticle = articleService.findEntityBySlug(commentCreateRequestDto.getSlug()).orElseThrow(() -> new EntityNotFoundException(
                "Article slug=" + commentCreateRequestDto.getSlug() + " không tồn tại"));
        User foundAuthor = userService.findUserByEmail(email).orElseThrow(() -> new EntityNotFoundException(
                "User email = " + email + " không tồn tại"));
        Comment newComment = commentRepo.save(new Comment(foundAuthor,foundArticle,commentCreateRequestDto.getBody()));
        return new CommentResponseDto(newComment.getId(),newComment.getBody(),newComment.getUpdatedAt(),newComment.getCreatedAt());
    }
}
