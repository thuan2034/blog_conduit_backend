package com.blog.conduit.controllers;

import com.blog.conduit.dtos.ArticleCreateRequestDto;
import com.blog.conduit.dtos.ArticlePageResponseDto;
import com.blog.conduit.dtos.ArticleResponseDto;
import com.blog.conduit.models.Article;
import com.blog.conduit.models.ResponseObject;
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
    public ResponseEntity<ResponseObject> getArticlePage(
            @RequestParam(value = "limit", defaultValue = "20") int limit, // Default 20
            @RequestParam(value = "offset", defaultValue = "0") int offset, // Default 0
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "favorited", required = false) String favorited
    ) {
        try{
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK","found articles",articleService.findAll(limit, offset, tag, author, favorited)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("false",e.getMessage(),""));

        }
    }

    @GetMapping("/feed")
    public ResponseEntity<ResponseObject> getFeedArticles(
            @RequestParam(value = "limit", defaultValue = "20") int limit, // Default 20
            @RequestParam(value = "offset", defaultValue = "0") int offset // Default 0
    ) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK","found feed",articleService.findFeedArticles(limit,offset)));
    } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("false",e.getMessage(),""));

        }
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<ResponseObject> getById(@PathVariable Integer id) {
//        Optional<ArticleResponseDto> foundArticle = articleService.findById(id);
//        return foundArticle.isPresent() ?
//                ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "found article", foundArticle)) :
//                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("NOT_FOUND", "can't find article with id=" + id, ""));
//    }

    @GetMapping("/{slug}")
    public ResponseEntity<ResponseObject> getBySlug(@PathVariable("slug") String slug) {
        Optional<ArticleResponseDto> foundArticle = articleService.findBySlug(slug);
        return foundArticle.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "found article", foundArticle)) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("NOT_FOUND", "can't find article with slug: " + slug, ""));
    }

    @PostMapping
    public ResponseEntity<ResponseObject> create(@RequestBody ArticleCreateRequestDto articleCreateRequestDto) {
        ArticleResponseDto newArticle = articleService.create(articleCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject("OK", "created new article", newArticle));
    }

    @PostMapping("/{slug}/favorite")
    public ResponseEntity<?> favoriteArticle(@PathVariable("slug") String slug) {
        Optional<ArticleResponseDto> foundArticle = articleService.findBySlug(slug);
        if (foundArticle.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("NOT_FOUND", "can't find article with slug: " + slug, ""));
        else
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK","favorited",articleService.favoriteArticle(slug)));
    }

    @DeleteMapping("/{slug}/favorite")
    public ResponseEntity<?> unFavoriteArticle(@PathVariable("slug") String slug) {
        Optional<ArticleResponseDto> foundArticle = articleService.findBySlug(slug);
        if (foundArticle.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("NOT_FOUND", "can't find article with slug: " + slug, ""));
        else
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK","un favorited",articleService.unFavoriteArticle(slug)));

    }
}
