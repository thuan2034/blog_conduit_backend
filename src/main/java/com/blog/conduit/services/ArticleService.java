package com.blog.conduit.services;

import com.blog.conduit.dtos.ArticleCreateRequestDto;
import com.blog.conduit.dtos.ArticlePageResponseDto;
import com.blog.conduit.dtos.ArticleResponseDto;
import com.blog.conduit.dtos.ProfileResponseDto;
import com.blog.conduit.models.*;
import com.blog.conduit.repositories.*;
import com.github.slugify.Slugify;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
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
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private final ArticleRepository articleRepo;
    private final UserService userService;
    private final FollowRepository followRepo;
    private final ArticleFavoriteRepository articleFavoriteRepo;
    private final ArticleTagService articleTagService;

    public ArticleService(ArticleRepository articleRepo, UserRepository userRepo, ArticleTagRepository articleTagRepository, EntityManager entityManager, UserService userService, FollowRepository followRepo, ArticleFavoriteRepository articleFavoriteRepo, ArticleTagService articleTagService) {
        this.articleRepo = articleRepo;
        this.entityManager = entityManager;
        this.userService = userService;
        this.followRepo = followRepo;
        this.articleFavoriteRepo = articleFavoriteRepo;
        this.articleTagService = articleTagService;
    }

    @Transactional
    public ArticlePageResponseDto findAll(int limit, int offset, String tag, String author, String favorited) {
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
            User currentUser = userService.findUserByEmail(email).orElseThrow(() -> new EntityNotFoundException("User email = " + email + " không tồn tại"));
            articleResponseDtoList.forEach(dto -> {
                User foundAuthor = userService.findByUserNameEntity(dto.getAuthor().getUserName())
                        .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy user"));
                boolean isFollowing = followRepo
                        .existsByFollowingUserAndFollowedUser(currentUser, foundAuthor);
                dto.getAuthor().setFollowing(isFollowing);
            });
            return new ArticlePageResponseDto(articleResponseDtoList, pageSize, currentPage, totalPages, totalArticles);
        }
    }

    @Transactional
    public ArticlePageResponseDto findFeedArticles(int limit, int offset) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Lấy email từ JWT subject
        User currentUser = userService.findUserByEmail(email).orElseThrow(() -> new EntityNotFoundException("User email = " + email + " không tồn tại hoặc chưa đăng nhập"));
        if (limit < 0) limit = 20; // Default limit
        if (offset < 0) offset = 0; // Default offset
        Sort sort = Sort.by("createdAt").descending();
        int page = offset / limit;
        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<Article> articlePage = articleRepo.findFeedArticles(currentUser, pageable);
        List<Article> articleList = articlePage.getContent();
        long totalArticles = articlePage.getTotalElements(); // Lấy TỔNG số bản ghi trên tất cả các trang
        int totalPages = articlePage.getTotalPages();       // Lấy TỔNG số trang
        int currentPage = articlePage.getNumber();          // Số của trang hiện tại (bắt đầu từ 0)
        int pageSize = articlePage.getSize();               // Kích thước trang (chính là limit)
        List<ArticleResponseDto> articleResponseDtoList = articleList.stream().map(this::mapToDto).toList();
        articleResponseDtoList.forEach(dto -> {
            dto.getAuthor().setFollowing(true);
        });
        return new ArticlePageResponseDto(articleResponseDtoList, pageSize, currentPage, totalPages, totalArticles);
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
            User currentUser = userService.findUserByEmail(email).orElseThrow(() -> new EntityNotFoundException("lỗi xác thực: email = " + email + " không tồn tại"));
            boolean isFollow = followRepo.existsByFollowingUserAndFollowedUser(currentUser, foundArticle.getAuthor());
            ArticleResponseDto articleResponseDto = mapToDto(foundArticle);
            articleResponseDto.getAuthor().setFollowing(isFollow);
            return Optional.of(articleResponseDto);
        }
    }

    @Transactional
    public Optional<ArticleResponseDto> findByTitle(String title) {
        return articleRepo.findByTitle(title).map(this::mapToDto);
    }

    @Transactional
    public ArticleResponseDto create(ArticleCreateRequestDto dto) {
        //0. Kiểm tra slug unique
        final Slugify slg = Slugify.builder().build();
        final String resultSlug = slg.slugify(dto.getTitle());
        if(articleRepo.existsBySlug(resultSlug)){
            throw new RuntimeException("title này bị trùng");
        }
        // 1. Lấy author
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User author = userService.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User email=" + email + " không tồn tại"));
        // 2. Khởi tạo Article
        Article newArticle = new Article();
        newArticle.setTitle(dto.getTitle());
        newArticle.setSlug(resultSlug);
        newArticle.setBody(dto.getBody());
        newArticle.setDescription(dto.getDescription());
        newArticle.setAuthor(author);
        Article saved = articleRepo.save(newArticle);
        for (String tagName : dto.getTagList()) {
            articleTagService.create(saved, tagName);
        }
        // 5. Trả về DTO (bạn có thể dùng saved để lấy ID, createdAt...)
        return new ArticleResponseDto(
                saved.getId(),
                saved.getSlug(),
                saved.getTitle(),
                saved.getBody(),
                false,
                saved.getDescription(),
                saved.getFavoritesCount(),
                new ProfileResponseDto(
                        author.getUserName(),
                        author.getBio(),
                        author.getImage()
                ),
                saved.getUpdatedAt(),
                saved.getCreatedAt(),
                dto.getTagList()
        );
    }

    @Transactional
    public ArticleResponseDto favoriteArticle(String slug) {
        // 1. Lấy user
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userService.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User email=" + email + " không tồn tại"));
        Article foundArticle = articleRepo.findBySlug(slug).orElseThrow(() -> new EntityNotFoundException("Không thấy bài viết với slug = " + slug));
        Optional<ArticleFavorite> articleFavorite = articleFavoriteRepo.findByUserAndArticle(currentUser, foundArticle);
        if (articleFavorite.isEmpty()) {
            articleFavoriteRepo.save(new ArticleFavorite(currentUser, foundArticle));
            ArticleResponseDto articleResponseDto = mapToDto(foundArticle);
            articleResponseDto.setFavorited(true);
            return articleResponseDto;
        } else return new ArticleResponseDto();
    }

    @Transactional
    public ArticleResponseDto unFavoriteArticle(String slug) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userService.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User email=" + email + " không tồn tại"));
        Article foundArticle = articleRepo.findBySlug(slug).orElseThrow(() -> new EntityNotFoundException("Không thấy bài viết với slug = " + slug));
        articleFavoriteRepo.findByUserAndArticle(currentUser, foundArticle)
                .ifPresent(articleFavoriteRepo::delete);
        foundArticle.decreseFavoriteCount();
        return mapToDto(foundArticle);
    }

    @Transactional
    public ArticleResponseDto updateArticle(ArticleCreateRequestDto dto, String slug){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userService.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User email=" + email + " không tồn tại"));
        Article foundArticle = articleRepo.findBySlug(slug).orElseThrow(()->new EntityNotFoundException("không tìm thấy bài viết với slug: "+ slug));
        if(!foundArticle.getAuthor().equals(currentUser)){
            throw new RuntimeException("Không có quyền sửa bài viết này");
        }
        final Slugify slg = Slugify.builder().build();
        final String resultSlug = slg.slugify(dto.getTitle());
        if(articleRepo.existsBySlug(resultSlug)){
            throw new RuntimeException("title này bị trùng");
        }
        foundArticle.setTitle(dto.getTitle());
        foundArticle.setSlug(resultSlug);
        foundArticle.setBody(dto.getBody());
        foundArticle.setDescription(dto.getDescription());
        articleRepo.save(foundArticle);
        return mapToDto(foundArticle);
    }

    private ArticleResponseDto mapToDto(Article article) {
        User author = article.getAuthor(); // Hibernate load trong cùng transaction
        ProfileResponseDto profileResponseDto = new ProfileResponseDto(author.getUserName(), author.getBio(), author.getImage());
        List<String> tagList = article.getTags().stream()
                .map(articleTag -> articleTag.getTag().getTagName())
                .toList();
        return new ArticleResponseDto(article.getId(),
                article.getSlug(),
                article.getTitle(),
                article.getBody(),
                false,
                article.getDescription(),
                article.getFavoritesCount(),
                profileResponseDto,
                article.getCreatedAt(),
                article.getUpdatedAt(),
                tagList);
    }

}
