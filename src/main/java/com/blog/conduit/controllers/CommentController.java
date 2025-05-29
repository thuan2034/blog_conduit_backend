package com.blog.conduit.controllers;

import com.blog.conduit.dtos.CommentCreateRequestDto;
import com.blog.conduit.dtos.CommentResponseDto;
import com.blog.conduit.models.Comment;
import com.blog.conduit.models.ResponseObject;
import com.blog.conduit.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResponseObject> findByArticleSlug(@PathVariable("slug") String slug){
        List<CommentResponseDto> commentResponseDtoList = commentService.findByArticleSlug(slug);
        return commentResponseDtoList.isEmpty()?
                ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK","no comment for this article with slug name: "+slug,"")):
                ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK","found comments",commentResponseDtoList));
    }

    @PostMapping
    public ResponseEntity<ResponseObject> create(@PathVariable("slug") String slug, @RequestBody CommentCreateRequestDto commentCreateRequestDto){
        commentCreateRequestDto.setSlug(slug);
        CommentResponseDto newCommentResponseDto = commentService.create(commentCreateRequestDto);
        return newCommentResponseDto!=null?
                ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject("OK","created comment",newCommentResponseDto)):
                ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ResponseObject("failed","not created",""));
    }
}
