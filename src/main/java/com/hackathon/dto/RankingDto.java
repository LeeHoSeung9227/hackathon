package com.hackathon.dto;

import java.time.LocalDateTime;

public class RankingDto {
    
    private Long id;
    private Long userId;
    private String scopeType;
    private Integer rankPosition;
    private String reward;
    private LocalDateTime recordedAt;
    
    // 추가 정보
    private String username;
    private String nickname;
    private Integer pointsTotal;
    private String membershipLevel;
    private Integer points;
    
    // 생성자
    public RankingDto() {}
    
    public RankingDto(Long userId, String scopeType, Integer rankPosition, String reward) {
        this.userId = userId;
        this.scopeType = scopeType;
        this.rankPosition = rankPosition;
        this.reward = reward;
        this.recordedAt = LocalDateTime.now();
    }
    
    // Getter와 Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getScopeType() { return scopeType; }
    public void setScopeType(String scopeType) { this.scopeType = scopeType; }
    
    public Integer getRankPosition() { return rankPosition; }
    public void setRankPosition(Integer rankPosition) { this.rankPosition = rankPosition; }
    
    public String getReward() { return reward; }
    public void setReward(String reward) { this.reward = reward; }
    
    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    
    public Integer getPointsTotal() { return pointsTotal; }
    public void setPointsTotal(Integer pointsTotal) { this.pointsTotal = pointsTotal; }
    
    public String getMembershipLevel() { return membershipLevel; }
    public void setMembershipLevel(String membershipLevel) { this.membershipLevel = membershipLevel; }
    
    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }
    
    // 누락된 메서드들 추가
    public void setRank(int rank) {
        this.rankPosition = rank;
    }
    
    public void setCampus(String campus) {
        // campus 필드가 없으므로 nickname에 저장
        this.nickname = campus;
    }
    
    public void setCollege(String college) {
        // college 필드가 없으므로 username에 저장
        this.username = college;
    }
    
    @Override
    public String toString() {
        return "RankingDto{" +
                "id=" + id +
                ", userId=" + userId +
                ", scopeType='" + scopeType + '\'' +
                ", rankPosition=" + rankPosition +
                ", reward='" + reward + '\'' +
                ", recordedAt=" + recordedAt +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", pointsTotal=" + pointsTotal +
                ", membershipLevel='" + membershipLevel + '\'' +
                ", points=" + points +
                '}';
    }
}

