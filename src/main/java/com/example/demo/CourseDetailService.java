package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CourseDetailService {
    
    @Autowired
    private CourseDetailRepository courseDetailRepository;
    
    // Get all course details
    public List<CourseDetail> getAllCourseDetails() {
        return courseDetailRepository.findAll();
    }
    
    // Get course detail by ID
    public Optional<CourseDetail> getCourseDetailById(Long id) {
        return courseDetailRepository.findById(id);
    }
    
    // Get course detail by course ID
    public Optional<CourseDetail> getCourseDetailByCourseId(Long courseId) {
        return courseDetailRepository.findByCourseId(courseId);
    }
    
    // Create a new course detail
    public CourseDetail createCourseDetail(CourseDetail courseDetail) {
        return courseDetailRepository.save(courseDetail);
    }
    
    // Update an existing course detail
    public CourseDetail updateCourseDetail(Long id, CourseDetail courseDetailData) {
        Optional<CourseDetail> courseDetail = courseDetailRepository.findById(id);
        if (courseDetail.isPresent()) {
            CourseDetail existingDetail = courseDetail.get();
            existingDetail.setCourseId(courseDetailData.getCourseId());
            existingDetail.setModules(courseDetailData.getModules());
            existingDetail.setReviews(courseDetailData.getReviews());
            existingDetail.setRating(courseDetailData.getRating());
            existingDetail.setStudents(courseDetailData.getStudents());
            existingDetail.setRatingDistribution(courseDetailData.getRatingDistribution());
            existingDetail.setOverview(courseDetailData.getOverview());
            return courseDetailRepository.save(existingDetail);
        }
        return null;
    }
    
    // Update course detail by course ID
    public CourseDetail updateCourseDetailByCourseId(Long courseId, CourseDetail courseDetailData) {
        Optional<CourseDetail> courseDetail = courseDetailRepository.findByCourseId(courseId);
        if (courseDetail.isPresent()) {
            CourseDetail existingDetail = courseDetail.get();
            existingDetail.setModules(courseDetailData.getModules());
            existingDetail.setReviews(courseDetailData.getReviews());
            existingDetail.setRating(courseDetailData.getRating());
            existingDetail.setStudents(courseDetailData.getStudents());
            existingDetail.setRatingDistribution(courseDetailData.getRatingDistribution());
            existingDetail.setOverview(courseDetailData.getOverview());
            return courseDetailRepository.save(existingDetail);
        }
        return null;
    }
    
    // Delete a course detail
    public boolean deleteCourseDetail(Long id) {
        if (courseDetailRepository.existsById(id)) {
            courseDetailRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Delete course detail by course ID
    @Transactional
    public boolean deleteCourseDetailByCourseId(Long courseId) {
        if (courseDetailRepository.existsByCourseId(courseId)) {
            courseDetailRepository.deleteByCourseId(courseId);
            return true;
        }
        return false;
    }
    
    // Check if course detail exists for a course
    public boolean existsByCourseId(Long courseId) {
        return courseDetailRepository.existsByCourseId(courseId);
    }
    
    // Get course detail count
    public long getCourseDetailCount() {
        return courseDetailRepository.count();
    }
}
