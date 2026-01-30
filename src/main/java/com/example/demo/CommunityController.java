package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/community")
@CrossOrigin(origins = {"http://localhost:5173", "https://*.vercel.app", "https://teachytechie.com", "https://www.teachytechie.com"}, allowCredentials = "true")
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
    public ResponseEntity<CommunityMessage> createMessage(
            @RequestParam("courseId") Long courseId,
            @RequestParam("courseTitle") String courseTitle,
            @RequestParam("userId") Long userId,
            @RequestParam("userName") String userName,
            @RequestParam("userEmail") String userEmail,
            @RequestParam("messageText") String messageText,
            @RequestParam("messageType") String messageType,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            CommunityMessage message = new CommunityMessage();
            message.setCourseId(courseId);
            message.setCourseTitle(courseTitle);
            message.setUserId(userId);
            message.setUserName(userName);
            message.setUserEmail(userEmail);
            message.setMessageText(messageText);
            message.setMessageType(messageType);
            
            // Handle file upload if present
            if (file != null && !file.isEmpty()) {
                String filename = file.getOriginalFilename();
                String filetype = file.getContentType();
                byte[] data = file.getBytes();
                
                message.setFilename(filename);
                message.setFiletype(filetype);
                message.setData(data);
            }
            
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
    
    // Get file data for a message
    @GetMapping("/messages/{messageId}/file")
    public ResponseEntity<byte[]> getMessageFile(@PathVariable Long messageId) {
        try {
            CommunityMessage message = communityService.getMessageById(messageId);
            if (message == null || message.getData() == null) {
                return ResponseEntity.notFound().build();
            }
            
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            if (message.getFiletype() != null) {
                headers.setContentType(org.springframework.http.MediaType.parseMediaType(message.getFiletype()));
            }
            if (message.getFilename() != null) {
                headers.setContentDispositionFormData("attachment", message.getFilename());
            }
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(message.getData());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
