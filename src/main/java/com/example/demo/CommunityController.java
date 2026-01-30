package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/community")
@CrossOrigin(origins = {"http://localhost:5173", "https://*.vercel.app"}, allowCredentials = "true")
public class CommunityController {
    
    @Autowired
    private CommunityService communityService;
    
    // Get all messages for a course
    @GetMapping("/messages/{courseId}")
    public ResponseEntity<List<CommunityMessage>> getMessagesByCourse(@PathVariable Long courseId) {
        try {
            List<CommunityMessage> messages = communityService.getMessagesByCourseId(courseId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Get message count for a course
    @GetMapping("/messages/count/{courseId}")
    public ResponseEntity<Map<String, Long>> getMessageCount(@PathVariable Long courseId) {
        try {
            long count = communityService.getMessageCountByCourseId(courseId);
            Map<String, Long> response = new HashMap<>();
            response.put("count", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Create a new message
    @PostMapping("/messages")
    public ResponseEntity<CommunityMessage> createMessage(@RequestBody CommunityMessage message) {
        try {
            CommunityMessage savedMessage = communityService.createMessage(message);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedMessage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Delete a message
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Map<String, String>> deleteMessage(@PathVariable Long messageId) {
        try {
            communityService.deleteMessage(messageId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Message deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Failed to delete message");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    // Upload attachment (simplified version - stores base64 in message)
    @PostMapping("/attachments")
    public ResponseEntity<CommunityAttachment> uploadAttachment(@RequestBody CommunityAttachment attachment) {
        try {
            CommunityAttachment savedAttachment = communityService.addAttachment(attachment);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAttachment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Get attachments for a message
    @GetMapping("/attachments/{messageId}")
    public ResponseEntity<List<CommunityAttachment>> getAttachments(@PathVariable Long messageId) {
        try {
            List<CommunityAttachment> attachments = communityService.getAttachmentsByMessageId(messageId);
            return ResponseEntity.ok(attachments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
