package com.blog.conduit.repositories;

import com.blog.conduit.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByUserName(String userName);
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailOrUserName(String email,String userName);
    boolean existsByEmailOrUserName(String email,String userName);
    boolean existsByEmail(String email);
    boolean existsByUserName(String userName);
}
