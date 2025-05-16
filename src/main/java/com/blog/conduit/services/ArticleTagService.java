package com.blog.conduit.services;

import com.blog.conduit.models.Article;
import com.blog.conduit.models.ArticleTag;
import com.blog.conduit.models.ArticleTagId;
import com.blog.conduit.models.Tag;
import com.blog.conduit.repositories.ArticleRepository;
import com.blog.conduit.repositories.ArticleTagRepository;
import com.blog.conduit.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleTagService {
    @Autowired
    private final ArticleTagRepository articleTagRepo;
    private final TagService tagService;

    public ArticleTagService(ArticleTagRepository articleTagRepo, TagService tagService) {
        this.articleTagRepo = articleTagRepo;
        this.tagService = tagService;
    }

    @Transactional
    public List<ArticleTag> findAll() {
        return articleTagRepo.findAll();
    }

//    @Transactional
//    public ArticleTag findByTagName(String tagName){
//        Optional<Tag> foundTag = tagService.findByTagName(tagName);
//    }

    @Transactional
    public ArticleTag create(Article article, String tagName) {
        Optional<Tag> foundTag = tagService.findByTagName(tagName);

        Tag tag = foundTag.orElseGet(() -> {
            Tag newTag = new Tag(tagName);
            return tagService.create(newTag);
        });

        ArticleTag newArticleTag = new ArticleTag(article, tag);
        return articleTagRepo.save(newArticleTag);
    }
}
