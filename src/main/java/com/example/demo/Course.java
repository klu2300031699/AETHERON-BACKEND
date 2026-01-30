package com.example.demo;

import jakarta.persistence.*;

@Entity
@Table(name = "courses")
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(length = 1000)
    private String description;
    
    @Column(nullable = false)
    private String category;
    
    private String duration;
    
    private String instructor;
    
    private String image;
    
    @Column(name = "original_price")
    private Integer originalPrice;
    
    private Integer price;
    
    private String color;
    
    // Default constructor
    public Course() {
    }
    
    // Constructor with all fields
    public Course(String title, String description, String category, String duration, 
                  String instructor, String image, Integer originalPrice, Integer price, String color) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.duration = duration;
        this.instructor = instructor;
        this.image = image;
        this.originalPrice = originalPrice;
        this.price = price;
        this.color = color;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getDuration() {
        return duration;
    }
    
    public void setDuration(String duration) {
        this.duration = duration;
    }
    
    public String getInstructor() {
        return instructor;
    }
    
    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }
    
    public String getImage() {
        return image;
    }
    
    public void setImage(String image) {
        this.image = image;
    }
    
    public Integer getOriginalPrice() {
        return originalPrice;
    }
    
    public void setOriginalPrice(Integer originalPrice) {
        this.originalPrice = originalPrice;
    }
    
    public Integer getPrice() {
        return price;
    }
    
    public void setPrice(Integer price) {
        this.price = price;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
}
