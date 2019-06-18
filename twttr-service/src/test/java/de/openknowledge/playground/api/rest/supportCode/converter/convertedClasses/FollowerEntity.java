package de.openknowledge.playground.api.rest.supportCode.converter.convertedClasses;

public class FollowerEntity {
    private Integer followerId;
    private Integer followingId;

    public FollowerEntity(Integer followerId, Integer followingId) {
        this.followerId = followerId;
        this.followingId = followingId;
    }

    public Integer getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Integer followerId) {
        this.followerId = followerId;
    }

    public Integer getFollowingId() {
        return followingId;
    }

    public void setFollowingId(Integer followingId) {
        this.followingId = followingId;
    }
}
