package com.hackathon.dto;

public class BadgeDto {
    
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Integer requiredPoints;
    private String category;
    
    // 생성자
    public BadgeDto() {}
    
    public BadgeDto(String name, String description, String imageUrl, Integer requiredPoints, String category) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.requiredPoints = requiredPoints;
        this.category = category;
    }
    
    // Getter와 Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public Integer getRequiredPoints() { return requiredPoints; }
    public void setRequiredPoints(Integer requiredPoints) { this.requiredPoints = requiredPoints; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    @Override
    public String toString() {
        return "BadgeDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", requiredPoints=" + requiredPoints +
                ", category='" + category + '\'' +
                '}';
    }
}
