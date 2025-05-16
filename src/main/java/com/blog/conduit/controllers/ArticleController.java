package com.blog.conduit.controllers;

import com.blog.conduit.dtos.ArticleCreateRequestDto;
import com.blog.conduit.dtos.ArticleResponseDto;
import com.blog.conduit.models.Article;
import com.blog.conduit.models.ResponseObject;
import com.blog.conduit.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/articles")
public class ArticleController {
    @Autowired
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public List<ArticleResponseDto> getAll(){
        return articleService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getById(@PathVariable Integer id){
        Optional<ArticleResponseDto> foundArticle =  articleService.findById(id);
        return foundArticle.isPresent()?
                ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK","found article",foundArticle)) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("NOT_FOUND","can't find article with id=" + id,""));
    }

    @PostMapping
    public ResponseEntity<ResponseObject> create(@RequestBody ArticleCreateRequestDto articleCreateRequestDto){
        Optional<Article> foundArticle = articleService.findBySlug(articleCreateRequestDto.getSlug());
        if(foundArticle.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ResponseObject("FAILED","slug name existed",""));
        Article newArticle = articleService.create(articleCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject("OK","created new article",newArticle));
    }
}
