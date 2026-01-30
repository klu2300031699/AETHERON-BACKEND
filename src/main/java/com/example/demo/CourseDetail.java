package com.example.demo;

import jakarta.persistence.*;

@Entity
@Table(name = "course_details")
public class CourseDetail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "course_id", nullable = false, unique = true)
    private Long courseId;
    
    @Column(columnDefinition = "JSON")
    private String modules;
    
    @Column(columnDefinition = "JSON")
    private String reviews;
    
    @Column(nullable = false)
    private Double rating;
    
    @Column(nullable = false)
    private String students;
    
    @Column(columnDefinition = "JSON")
    private String ratingDistribution;
    
    @Column(columnDefinition = "JSON")
    private String overview;
    
    // Default constructor
    public CourseDetail() {
    }
    
    // Constructor with all fields
    public CourseDetail(Long courseId, String modules, String reviews, Double rating, 
                        String students, String ratingDistribution, String overview) {
        this.courseId = courseId;
        this.modules = modules;
        this.reviews = reviews;
        this.rating = rating;
        this.students = students;
        this.ratingDistribution = ratingDistribution;
        this.overview = overview;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getCourseId() {
        return courseId;
    }
    
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
    
    public String getModules() {
        return modules;
    }
    
    public void setModules(String modules) {
        this.modules = modules;
    }
    
    public String getReviews() {
        return reviews;
    }
    
    public void setReviews(String reviews) {
        this.reviews = reviews;
    }
    
    public Double getRating() {
        return rating;
    }
    
    public void setRating(Double rating) {
        this.rating = rating;
    }
    
    public String getStudents() {
        return students;
    }
    
    public void setStudents(String students) {
        this.students = students;
    }
    
    public String getRatingDistribution() {
        return ratingDistribution;
    }
    
    public void setRatingDistribution(String ratingDistribution) {
        this.ratingDistribution = ratingDistribution;
    }
    
    public String getOverview() {
        return overview;
    }
    
    public void setOverview(String overview) {
        this.overview = overview;
    }
}
