package com.blog.conduit.services;

import com.blog.conduit.dtos.ArticleCreateRequestDto;
import com.blog.conduit.dtos.ArticlePageResponseDto;
import com.blog.conduit.dtos.ArticleResponseDto;
import com.blog.conduit.dtos.ProfileResponseDto;
import com.blog.conduit.models.Article;
import com.blog.conduit.models.ArticleTag;
import com.blog.conduit.models.Follow;
import com.blog.conduit.models.User;
import com.blog.conduit.repositories.ArticleRepository;
import com.blog.conduit.repositories.ArticleTagRepository;
import com.blog.conduit.repositories.FollowRepository;
import com.blog.conduit.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleService {
    @Autowired
    private final ArticleRepository articleRepo;
    @Autowired
    private final UserRepository userRepo;
    @Autowired
    private final ArticleTagRepository articleTagRepository;
    private final FollowRepository followRepo;

    public ArticleService(ArticleRepository articleRepo, UserRepository userRepo, ArticleTagRepository articleTagRepository, FollowRepository followRepo) {
        this.articleRepo = articleRepo;
        this.userRepo = userRepo;
        this.articleTagRepository = articleTagRepository;
        this.followRepo = followRepo;
    }

    @Transactional
    public ArticlePageResponseDto findAll(int limit, int offset) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Lấy email từ JWT subject
        if (limit < 0) limit = 20; // Default limit
        if (offset < 0) offset = 0; // Default offset
        Sort sort = Sort.by("createdAt").descending();
        int page = offset / limit;
        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<Article> articlePage = articleRepo.findAll(pageable);
        List<Article> articleList = articlePage.getContent(); // Lấy danh sách bài viết của trang hiện tại
        long totalArticles = articlePage.getTotalElements(); // Lấy TỔNG số bản ghi trên tất cả các trang
        int totalPages = articlePage.getTotalPages();       // Lấy TỔNG số trang
        int currentPage = articlePage.getNumber();          // Số của trang hiện tại (bắt đầu từ 0)
        int pageSize = articlePage.getSize();               // Kích thước trang (chính là limit)
            List<ArticleResponseDto> articleResponseDtoList = articleList.stream().map(this::mapToDto).toList();
        if (email == null || email.equals("anonymousUser")) {//nếu chưa đăng nhập, không cần kiểm tra trạng thái follow
            return new ArticlePageResponseDto(articleResponseDtoList, pageSize, currentPage, totalPages, totalArticles);
        } else {
            User currentUser = userRepo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User email = " + email + " không tồn tại"));
            articleResponseDtoList.forEach(dto -> {
                User author = userRepo.findByUserName(dto.getAuthor().getUserName())
                        .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy user"));
                boolean isFollowing = followRepo
                        .findByFollowingUserAndFollowedUser(currentUser, author)
                        .isPresent();
                dto.getAuthor().setFollowing(isFollowing);
            });
            return new ArticlePageResponseDto(articleResponseDtoList, pageSize, currentPage, totalPages, totalArticles);
        }
    }


    @Transactional
    public Optional<ArticleResponseDto> findById(Integer id) {
        return articleRepo.findById(id).map(this::mapToDto);
    }

    @Transactional
    public Optional<Article> findEntityBySlug(String slug) {
        return articleRepo.findBySlug(slug);
    }

    @Transactional
    public Optional<ArticleResponseDto> findBySlug(String slug) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Lấy email từ JWT subject
        if (email == null || email.equals("anonymousUser")) {
            return articleRepo.findBySlug(slug).map(this::mapToDto);
        } else {
            Article foundArticle = articleRepo.findBySlug(slug).orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bài viết"));
            User currentUser = userRepo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User email = " + email + " không tồn tại"));
            Optional<Follow> isFollow = followRepo.findByFollowingUserAndFollowedUser(currentUser, foundArticle.getAuthor());
            ArticleResponseDto articleResponseDto = mapToDto(foundArticle);
            if (isFollow.isPresent()) {
                articleResponseDto.getAuthor().setFollowing(true);
                return Optional.of(articleResponseDto);
            } else return Optional.of(articleResponseDto);
        }
    }

    @Transactional
    public Optional<ArticleResponseDto> findByTitle(String title) {
        return articleRepo.findByTitle(title).map(this::mapToDto);
    }

    @Transactional
    public Article create(ArticleCreateRequestDto articleCreateRequestDto) {
        User author = userRepo.findById(articleCreateRequestDto.getAuthorId()).orElseThrow(() -> new EntityNotFoundException("User id=" + articleCreateRequestDto.getAuthorId() + " không tồn tại"));
        Article newArticle = new Article();
        newArticle.setTitle(articleCreateRequestDto.getTitle());
        newArticle.setSlug(articleCreateRequestDto.getSlug());
        newArticle.setBody(articleCreateRequestDto.getBody());
        newArticle.setDescription(articleCreateRequestDto.getDescription());
        newArticle.setAuthor(author);
        return articleRepo.save(newArticle);
    }

    private ArticleResponseDto mapToDto(Article article) {
        User author = article.getAuthor(); // Hibernate load trong cùng transaction
        // Build ProfileResponseDto
        ProfileResponseDto profileResponseDto = new ProfileResponseDto(author.getUserName(), author.getBio(), author.getImage());
        // Build ArticleResponseDto
        ArticleResponseDto articleResponseDto = new ArticleResponseDto(article.getId(), article.getSlug(), article.getTitle(), article.getDescription(), article.getBody(), article.getFavoritesCount(), profileResponseDto, article.getCreatedAt(), article.getUpdatedAt());
        List<ArticleTag> articleTagList = articleTagRepository.findByArticle(article);

        List<String> tagName = articleTagList.stream().map(articleTag -> articleTag.getTag().getTagName()).collect(Collectors.toList()); // Sửa lỗi ở đây

        articleResponseDto.setTagList(tagName);
        return articleResponseDto;
    }
}
