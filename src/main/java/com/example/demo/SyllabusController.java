package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/syllabus")
@CrossOrigin(origins = {"http://localhost:5173", "https://*.vercel.app", "https://teachytechie.com", "https://www.teachytechie.com"}, allowCredentials = "true")
public class SyllabusController {
    
    @Autowired
    private SyllabusService syllabusService;
    
    // Get all syllabus entries (Admin only)
    @GetMapping("/all")
    public ResponseEntity<List<Syllabus>> getAllSyllabus() {
        try {
            List<Syllabus> syllabusList = syllabusService.getAllSyllabus();
            return ResponseEntity.ok(syllabusList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    // Get syllabus by ID
    @GetMapping("/{id}")
    public ResponseEntity<Syllabus> getSyllabusById(@PathVariable Long id) {
        Optional<Syllabus> syllabus = syllabusService.getSyllabusById(id);
        return syllabus.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    // Get all syllabus for a course (Admin - includes drafts)
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Syllabus>> getSyllabusByCourseId(@PathVariable Long courseId) {
        try {
            List<Syllabus> syllabusList = syllabusService.getSyllabusByCourseId(courseId);
            return ResponseEntity.ok(syllabusList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    // Get published syllabus for a course (Student view)
    @GetMapping("/course/{courseId}/published")
    public ResponseEntity<List<Syllabus>> getPublishedSyllabusByCourseId(@PathVariable Long courseId) {
        try {
            List<Syllabus> syllabusList = syllabusService.getPublishedSyllabusByCourseId(courseId);
            return ResponseEntity.ok(syllabusList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    // Get syllabus by course, module, and topic (Student view - published only)
    @GetMapping("/course/{courseId}/module/{moduleId}/topic/{topicName}")
    public ResponseEntity<Syllabus> getSyllabusByDetails(
            @PathVariable Long courseId, 
            @PathVariable Integer moduleId, 
            @PathVariable String topicName) {
        try {
            Optional<Syllabus> syllabus = syllabusService.getSyllabusByDetails(courseId, moduleId, topicName);
            if (syllabus.isPresent() && "PUBLISHED".equals(syllabus.get().getStatus())) {
                return ResponseEntity.ok(syllabus.get());
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    // Get syllabus by course, module, and topic (Admin - includes drafts)
    @GetMapping("/admin/course/{courseId}/module/{moduleId}/topic/{topicName}")
    public ResponseEntity<Syllabus> getAdminSyllabusByDetails(
            @PathVariable Long courseId, 
            @PathVariable Integer moduleId, 
            @PathVariable String topicName) {
        try {
            Optional<Syllabus> syllabus = syllabusService.getSyllabusByDetails(courseId, moduleId, topicName);
            return syllabus.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    // Get syllabus for a module
    @GetMapping("/course/{courseId}/module/{moduleId}")
    public ResponseEntity<List<Syllabus>> getSyllabusByModule(
            @PathVariable Long courseId, 
            @PathVariable Integer moduleId) {
        try {
            List<Syllabus> syllabusList = syllabusService.getSyllabusByModule(courseId, moduleId);
            return ResponseEntity.ok(syllabusList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    // Create or update syllabus
    @PostMapping("/save")
    public ResponseEntity<?> saveSyllabus(@RequestBody Syllabus syllabus) {
        try {
            Syllabus savedSyllabus = syllabusService.saveSyllabus(syllabus);
            return ResponseEntity.ok(savedSyllabus);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Error saving syllabus: " + e.getMessage());
        }
    }
    
    // Update syllabus status
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id, 
            @RequestParam String status,
            @RequestParam String updatedBy) {
        try {
            Syllabus updatedSyllabus = syllabusService.updateStatus(id, status, updatedBy);
            if (updatedSyllabus != null) {
                return ResponseEntity.ok(updatedSyllabus);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Error updating status: " + e.getMessage());
        }
    }
    
    // Delete syllabus
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSyllabus(@PathVariable Long id) {
        try {
            boolean deleted = syllabusService.deleteSyllabus(id);
            if (deleted) {
                return ResponseEntity.ok("Syllabus deleted successfully");
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Error deleting syllabus: " + e.getMessage());
        }
    }
    
    // Get syllabus statistics
    @GetMapping("/stats")
    public ResponseEntity<?> getSyllabusStats() {
        try {
            long totalCount = syllabusService.getSyllabusCount();
            long publishedCount = syllabusService.getPublishedCount();
            return ResponseEntity.ok(new SyllabusStats(totalCount, publishedCount));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Error getting stats: " + e.getMessage());
        }
    }
}

// Stats DTO
class SyllabusStats {
    private long total;
    private long published;
    
    public SyllabusStats(long total, long published) {
        this.total = total;
        this.published = published;
    }
    
    public long getTotal() {
        return total;
    }
    
    public void setTotal(long total) {
        this.total = total;
    }
    
    public long getPublished() {
        return published;
    }
    
    public void setPublished(long published) {
        this.published = published;
    }
}
