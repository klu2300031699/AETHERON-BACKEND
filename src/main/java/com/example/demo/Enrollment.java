package com.example.demo;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String userName;
    private String userEmail;
    private String userMobileNumber;
    private Long courseId;
    private String courseTitle;
    private String courseCategory;
    private Integer coursePrice;
    private LocalDateTime enrolledAt;
    private Integer progress = 0; // Progress percentage 0-100
    
    @Column(columnDefinition = "TEXT")
    private String completedTopics; // JSON array of completed topic names

    public Enrollment() {
        this.enrolledAt = LocalDateTime.now();
        this.completedTopics = "[]"; // Initialize as empty JSON array
    }

    // Getters and Setters datailed below
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getUserMobileNumber() { return userMobileNumber; }
    public void setUserMobileNumber(String userMobileNumber) { this.userMobileNumber = userMobileNumber; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public String getCourseTitle() { return courseTitle; }
    public void setCourseTitle(String courseTitle) { this.courseTitle = courseTitle; }

    public String getCourseCategory() { return courseCategory; }
    public void setCourseCategory(String courseCategory) { this.courseCategory = courseCategory; }

    public Integer getCoursePrice() { return coursePrice; }
    public void setCoursePrice(Integer coursePrice) { this.coursePrice = coursePrice; }

    public LocalDateTime getEnrolledAt() { return enrolledAt; }
    public void setEnrolledAt(LocalDateTime enrolledAt) { this.enrolledAt = enrolledAt; }

    public Integer getProgress() { return progress; }
    public void setProgress(Integer progress) { this.progress = progress; }
    
    public String getCompletedTopics() { return completedTopics; }
    public void setCompletedTopics(String completedTopics) { this.completedTopics = completedTopics; }
}
