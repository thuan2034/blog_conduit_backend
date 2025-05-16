package com.blog.conduit.repositories;

import com.blog.conduit.models.Follow;
import com.blog.conduit.models.FollowId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, FollowId> {
}
