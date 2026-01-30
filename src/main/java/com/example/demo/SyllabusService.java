package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SyllabusService {
    
    @Autowired
    private SyllabusRepository syllabusRepository;
    
    // Get all syllabus entries
    public List<Syllabus> getAllSyllabus() {
        return syllabusRepository.findAll();
    }
    
    // Get syllabus by ID
    public Optional<Syllabus> getSyllabusById(Long id) {
        return syllabusRepository.findById(id);
    }
    
    // Get all syllabus for a course
    public List<Syllabus> getSyllabusByCourseId(Long courseId) {
        return syllabusRepository.findByCourseId(courseId);
    }
    
    // Get published syllabus for a course
    public List<Syllabus> getPublishedSyllabusByCourseId(Long courseId) {
        return syllabusRepository.findByCourseIdAndStatus(courseId, "PUBLISHED");
    }
    
    // Get syllabus by course, module, and topic
    public Optional<Syllabus> getSyllabusByDetails(Long courseId, Integer moduleId, String topicName) {
        return syllabusRepository.findByCourseIdAndModuleIdAndTopicName(courseId, moduleId, topicName);
    }
    
    // Get syllabus for a module
    public List<Syllabus> getSyllabusByModule(Long courseId, Integer moduleId) {
        return syllabusRepository.findByCourseIdAndModuleId(courseId, moduleId);
    }
    
    // Create or update syllabus
    @Transactional
    public Syllabus saveSyllabus(Syllabus syllabus) {
        // Check if syllabus already exists
        Optional<Syllabus> existing = syllabusRepository.findByCourseIdAndModuleIdAndTopicName(
            syllabus.getCourseId(), 
            syllabus.getModuleId(), 
            syllabus.getTopicName()
        );
        
        if (existing.isPresent()) {
            // Update existing
            Syllabus existingSyllabus = existing.get();
            existingSyllabus.setContent(syllabus.getContent());
            existingSyllabus.setStatus(syllabus.getStatus());
            existingSyllabus.setUpdatedBy(syllabus.getUpdatedBy());
            existingSyllabus.setModuleTitle(syllabus.getModuleTitle());
            return syllabusRepository.save(existingSyllabus);
        } else {
            // Create new
            return syllabusRepository.save(syllabus);
        }
    }
    
    // Update syllabus status
    @Transactional
    public Syllabus updateStatus(Long id, String status, String updatedBy) {
        Optional<Syllabus> syllabusOpt = syllabusRepository.findById(id);
        if (syllabusOpt.isPresent()) {
            Syllabus syllabus = syllabusOpt.get();
            syllabus.setStatus(status);
            syllabus.setUpdatedBy(updatedBy);
            return syllabusRepository.save(syllabus);
        }
        return null;
    }
    
    // Delete syllabus
    @Transactional
    public boolean deleteSyllabus(Long id) {
        if (syllabusRepository.existsById(id)) {
            syllabusRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Get syllabus count
    public long getSyllabusCount() {
        return syllabusRepository.count();
    }
    
    // Get published count
    public long getPublishedCount() {
        return syllabusRepository.findByStatus("PUBLISHED").size();
    }
}
