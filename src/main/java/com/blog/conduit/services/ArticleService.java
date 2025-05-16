package com.blog.conduit.services;

import com.blog.conduit.dtos.ArticleCreateRequestDto;
import com.blog.conduit.dtos.ArticleResponseDto;
import com.blog.conduit.dtos.AuthorDto;
import com.blog.conduit.models.Article;
import com.blog.conduit.models.User;
import com.blog.conduit.repositories.ArticleRepository;
import com.blog.conduit.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    @Autowired
    private final ArticleRepository articleRepo;
    @Autowired
    private final UserRepository userRepo;

    public ArticleService(ArticleRepository articleRepo, UserRepository userRepo) {
        this.articleRepo = articleRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public List<ArticleResponseDto> findAll() {
        return articleRepo.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }
    private ArticleResponseDto mapToDto(Article article) {
        User author = article.getAuthor(); // Hibernate load trong cùng transaction
        // Build AuthorDto
        AuthorDto authorDto = new AuthorDto(
                author.getUserName(),
                author.getBio(),
                author.getImage()
        );
        // Build ArticleResponseDto
        return new ArticleResponseDto(
                article.getId(),
                article.getSlug(),
                article.getTitle(),
                article.getDescription(),
                article.getBody(),
                article.getFavoritesCount(),
                authorDto,
                article.getCreatedAt(),
                article.getUpdatedAt()
        );
    }

    @Transactional
    public Optional<ArticleResponseDto> findById(Integer id) {
        return articleRepo.findById(id)
                .map(this::mapToDto);
    }

    @Transactional
    public Optional<Article> findBySlug(String slug) {
        return articleRepo.findBySlug(slug);
    }

    @Transactional
    public List<Article> findByTitle(String title) {
        return articleRepo.findByTitle(title);
    }

    @Transactional
    public Article create(ArticleCreateRequestDto articleCreateRequestDto) {
        User author = userRepo.findById(articleCreateRequestDto.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "User id=" + articleCreateRequestDto.getAuthorId() + " không tồn tại"));
        Article newArticle = new Article();
        newArticle.setTitle(articleCreateRequestDto.getTitle());
        newArticle.setSlug(articleCreateRequestDto.getSlug());
        newArticle.setBody(articleCreateRequestDto.getBody());
        newArticle.setDescription(articleCreateRequestDto.getDescription());
        newArticle.setAuthor(author);
        return articleRepo.save(newArticle);
    }
}
