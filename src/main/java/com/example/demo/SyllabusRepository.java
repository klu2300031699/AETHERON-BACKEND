package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SyllabusRepository extends JpaRepository<Syllabus, Long> {
    
    // Find all syllabus entries for a course
    List<Syllabus> findByCourseId(Long courseId);
    
    // Find syllabus by course, module, and topic
    Optional<Syllabus> findByCourseIdAndModuleIdAndTopicName(Long courseId, Integer moduleId, String topicName);
    
    // Find all published syllabus for a course
    List<Syllabus> findByCourseIdAndStatus(Long courseId, String status);
    
    // Find all syllabus for a module
    List<Syllabus> findByCourseIdAndModuleId(Long courseId, Integer moduleId);
    
    // Find all syllabus by status
    List<Syllabus> findByStatus(String status);
}
