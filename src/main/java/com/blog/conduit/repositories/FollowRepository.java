package com.blog.conduit.repositories;

import com.blog.conduit.models.Follow;
import com.blog.conduit.models.FollowId;
import com.blog.conduit.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, FollowId> {
    public List<Follow> findByFollowingUser(User user);
    public List<Follow> findByFollowedUser(User user);
    public Optional<Follow> findByFollowingUserAndFollowedUser(User followingUser,User followedUser);
    public boolean existsByFollowingUserAndFollowedUser(User followingUser,User followesUser);
}
