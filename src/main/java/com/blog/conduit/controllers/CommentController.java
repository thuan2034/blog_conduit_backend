package com.blog.conduit.controllers;

import com.blog.conduit.dtos.CommentCreateRequestDto;
import com.blog.conduit.dtos.CommentResponseDto;
import com.blog.conduit.models.Comment;
import com.blog.conduit.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles/{slug}/comments")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public List<CommentResponseDto> findByArticleSlug(@PathVariable("slug") String slug){
        return commentService.findByArticleSlug(slug);
    }

    @PostMapping
    public CommentResponseDto create(@PathVariable("slug") String slug, @RequestBody CommentCreateRequestDto commentCreateRequestDto){
        commentCreateRequestDto.setSlug(slug);
        return commentService.create(commentCreateRequestDto);
    }
}
