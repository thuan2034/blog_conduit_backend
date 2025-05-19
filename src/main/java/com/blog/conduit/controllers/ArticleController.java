package com.blog.conduit.controllers;

import com.blog.conduit.dtos.ArticleCreateRequestDto;
import com.blog.conduit.dtos.ArticlePageResponseDto;
import com.blog.conduit.dtos.ArticleResponseDto;
import com.blog.conduit.models.Article;
import com.blog.conduit.models.ResponseObject;
import com.blog.conduit.models.Tag;
import com.blog.conduit.services.ArticleService;
import com.blog.conduit.services.ArticleTagService;
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
    @Autowired
    private final ArticleTagService articleTagService;

    public ArticleController(ArticleService articleService, ArticleTagService articleTagService) {
        this.articleService = articleService;
        this.articleTagService = articleTagService;
    }

    @GetMapping
    public ArticlePageResponseDto getArticlePage(
            @RequestParam(value = "limit", defaultValue = "20") int limit, // Default 20
            @RequestParam(value = "offset", defaultValue = "0") int offset // Default 0
    ) {
        return articleService.findAll(limit,offset);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<ResponseObject> getById(@PathVariable Integer id) {
//        Optional<ArticleResponseDto> foundArticle = articleService.findById(id);
//        return foundArticle.isPresent() ?
//                ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "found article", foundArticle)) :
//                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("NOT_FOUND", "can't find article with id=" + id, ""));
//    }

    @GetMapping("/{slug}")
    public ResponseEntity<ResponseObject> getBySlug(@PathVariable("slug") String slug){
      Optional<ArticleResponseDto> foundArticle = articleService.findBySlug(slug);
      return foundArticle.isPresent()?
              ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "found article", foundArticle)) :
              ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("NOT_FOUND", "can't find article with slug: " + slug, ""));
    }

    @PostMapping
    public ResponseEntity<ResponseObject> create(@RequestBody ArticleCreateRequestDto articleCreateRequestDto) {
        Optional<ArticleResponseDto> foundArticle = articleService.findBySlug(articleCreateRequestDto.getSlug());
        if (foundArticle.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ResponseObject("FAILED", "slug name existed", ""));
        Article newArticle = articleService.create(articleCreateRequestDto);
        List<String> tagList = articleCreateRequestDto.getTagList();
        if (tagList != null) {
            for (String tagName : tagList) {
                articleTagService.create(newArticle, tagName.trim());
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject("OK", "created new article", newArticle));
    }

    @PostMapping("/{slug}/favorite")
    public ResponseEntity<?> favoriteArticle(@PathVariable("slug") String slug){
        return null;
    }

    @DeleteMapping("/{slug}/favorite")
    public  ResponseEntity<?> unFavoriteArticle(@PathVariable("slug") String slug){
        return null;
    }
}
