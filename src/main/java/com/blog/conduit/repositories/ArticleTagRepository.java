package com.blog.conduit.repositories;

import com.blog.conduit.models.ArticleTag;
import com.blog.conduit.models.ArticleTagId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleTagRepository extends JpaRepository<ArticleTag, ArticleTagId> {

}
