package com.hackathon.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = true)
    private String pw;
    
    @Column(name = "pw_hash", nullable = false)
    private String pwHash;
    
    @Column(name = "pw_salt")
    private String pwSalt;
    
    @Column(name = "password_algo", length = 50)
    private String passwordAlgo;
    
    @Column(nullable = false)
    private String nickname;
    
    @Column(nullable = false)
    private String campus;
    
    @Column(nullable = false)
    private String level;
    
    @Column(name = "points_total")
    private Integer pointsTotal = 0;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(columnDefinition = "varchar default 'active'")
    private String status = "active";
    
    @Column(columnDefinition = "varchar default 'user'")
    private String role = "user";
    
    // 기존 필드들 (호환성을 위해 유지)
    @Column(unique = true)
    private String email;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    private String address;
    
    @Column(name = "membership_level")
    private String membershipLevel = "BRONZE";
    
    private Integer rank = 0;
    
    // Getter/Setter 메서드들
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getPoints() {
        return pointsTotal;
    }
    
    public void setPoints(Integer points) {
        this.pointsTotal = points;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getNickname() {
        return nickname;
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public String getCampus() {
        return campus;
    }
    
    public void setCampus(String campus) {
        this.campus = campus;
    }
    
    public String getMembershipLevel() {
        return membershipLevel;
    }
    
    public void setMembershipLevel(String membershipLevel) {
        this.membershipLevel = membershipLevel;
    }
    
    public Integer getRank() {
        return rank;
    }
    
    public void setRank(Integer rank) {
        this.rank = rank;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    // 누락된 메서드들 추가
    public void setPassword(String password) {
        this.pwHash = password;
    }
    
    public String getCollege() {
        return this.campus; // campus를 college로 매핑
    }
    
    public void setCollege(String college) {
        this.campus = college; // college를 campus로 매핑
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Spring Security UserDetails 구현
    @Override
    public String getUsername() {
        return username;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
    }
    
    @Override
    public String getPassword() {
        return pwHash;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return "active".equals(status);
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return "active".equals(status);
    }
}
