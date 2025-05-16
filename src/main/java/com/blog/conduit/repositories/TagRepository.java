package com.blog.conduit.repositories;

import com.blog.conduit.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag,Integer> {
    Optional<Tag> findByTagName(String tagName);
}
