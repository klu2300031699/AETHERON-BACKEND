package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    
    @Autowired
    private CourseRepository courseRepository;
    
    // Get all courses
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    
    // Get course by ID
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }
    
    // Get courses by category
    public List<Course> getCoursesByCategory(String category) {
        return courseRepository.findByCategory(category);
    }
    
    // Search courses by title
    public List<Course> searchCoursesByTitle(String title) {
        return courseRepository.findByTitleContainingIgnoreCase(title);
    }
    
    // Create a new course
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }
    
    // Update an existing course
    public Course updateCourse(Long id, Course courseDetails) {
        Optional<Course> course = courseRepository.findById(id);
        if (course.isPresent()) {
            Course existingCourse = course.get();
            existingCourse.setTitle(courseDetails.getTitle());
            existingCourse.setDescription(courseDetails.getDescription());
            existingCourse.setCategory(courseDetails.getCategory());
            existingCourse.setDuration(courseDetails.getDuration());
            existingCourse.setInstructor(courseDetails.getInstructor());
            existingCourse.setImage(courseDetails.getImage());
            existingCourse.setOriginalPrice(courseDetails.getOriginalPrice());
            existingCourse.setPrice(courseDetails.getPrice());
            existingCourse.setColor(courseDetails.getColor());
            return courseRepository.save(existingCourse);
        }
        return null;
    }
    
    // Delete a course
    public boolean deleteCourse(Long id) {
        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Get course count
    public long getCourseCount() {
        return courseRepository.count();
    }
}
