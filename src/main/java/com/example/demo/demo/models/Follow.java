package com.example.demo.demo.models;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="follow")
public class Follow {
    @EmbeddedId
    private FollowId id;

    @ManyToOne
    @MapsId("followingId")
    @JoinColumn(name="following_user_id")
    private User followingUser;

    @ManyToOne
    @MapsId("followedId")
    @JoinColumn(name = "followed_user_id")
    private  User followedUser;

    @CreatedDate
    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime createdAt;

    //constructors
    public Follow(){}
    public Follow(User followedUser,User followingUser, OffsetDateTime createdAt){
        this.followedUser=followedUser;
        this.followingUser=followingUser;
        this.createdAt=createdAt;
        this.id=new FollowId(followedUser.getId(),followingUser.getId());
    }

    //setters and getters...

    public FollowId getId() {
        return id;
    }

    public void setId(FollowId id) {
        this.id = id;
    }

    public User getFollowingUser() {
        return followingUser;
    }

    public void setFollowingUser(User followingUser) {
        this.followingUser = followingUser;
    }

    public User getFollowedUser() {
        return followedUser;
    }

    public void setFollowedUser(User followedUser) {
        this.followedUser = followedUser;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
