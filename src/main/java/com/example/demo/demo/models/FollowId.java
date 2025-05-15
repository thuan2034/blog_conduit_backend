package com.example.demo.demo.models;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FollowId implements Serializable {
    private Integer followingId;
    private Integer followedId;

    //constructors...
    public FollowId(){}
    public FollowId(Integer followedId,Integer followingId){
        this.followedId=followedId;
        this.followingId=followingId;
    }

    //getters...

    public Integer getFollowingId() {
        return followingId;
    }

    public Integer getFollowedId() {
        return followedId;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o==null || getClass()!=o.getClass()) return false;
        FollowId that = (FollowId) o;
        return Objects.equals(this.followedId,that.followedId) && Objects.equals(this.followingId,that.followingId);
    }

    @Override
    public int hashCode(){
        return Objects.hash(followedId,followingId);
    }
}
