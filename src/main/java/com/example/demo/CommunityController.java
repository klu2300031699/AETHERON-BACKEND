package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/community")
@CrossOrigin(origins = "*")
public class CommunityController {

    @Autowired
    private CommunityService communityService;

    // Get user's enrolled course communities
    @GetMapping("/user/{userId}/communities")
    public ResponseEntity<?> getUserCommunities(@PathVariable Long userId) {
        try {
            List<Map<String, Object>> communities = communityService.getUserCommunities(userId);
            return ResponseEntity.ok(communities);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error fetching communities: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Send message to community
    @PostMapping("/message")
    public ResponseEntity<?> sendMessage(@RequestBody CommunityMessage message) {
        try {
            CommunityMessage savedMessage = communityService.sendMessage(message);
            return ResponseEntity.ok(savedMessage);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error sending message: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Get messages for a course community
    @GetMapping("/course/{courseId}/messages")
    public ResponseEntity<?> getCourseMessages(@PathVariable Long courseId) {
        try {
            List<Map<String, Object>> messages = communityService.getCourseMessages(courseId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error fetching messages: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Upload file attachment
    @PostMapping("/attachment")
    public ResponseEntity<?> uploadAttachment(
            @RequestParam("messageId") Long messageId,
            @RequestParam("file") MultipartFile file) {
        try {
            // Validate file size (10MB max)
            if (file.getSize() > 10 * 1024 * 1024) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "File size exceeds 10MB limit");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            // Validate file type
            String contentType = file.getContentType();
            if (contentType == null || !isValidFileType(contentType)) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Invalid file type. Allowed: PDF, PPT, PPTX, PNG, JPG, JPEG, GIF");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            CommunityAttachment attachment = communityService.saveAttachment(messageId, file);
            return ResponseEntity.ok(attachment);
        } catch (IOException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error uploading file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Download attachment
    @GetMapping("/attachment/{id}")
    public ResponseEntity<?> downloadAttachment(@PathVariable Long id) {
        try {
            CommunityAttachment attachment = communityService.getAttachment(id);
            Path filePath = Paths.get(attachment.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(attachment.getFileType()))
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + attachment.getFileName() + "\"")
                        .body(resource);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("message", "File not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error downloading file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Get attachments for a message
    @GetMapping("/message/{messageId}/attachments")
    public ResponseEntity<?> getMessageAttachments(@PathVariable Long messageId) {
        try {
            List<CommunityAttachment> attachments = communityService.getMessageAttachments(messageId);
            return ResponseEntity.ok(attachments);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error fetching attachments: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Helper method to validate file types
    private boolean isValidFileType(String contentType) {
        return contentType.equals("application/pdf") ||
                contentType.equals("application/vnd.ms-powerpoint") ||
                contentType.equals("application/vnd.openxmlformats-officedocument.presentationml.presentation") ||
                contentType.equals("image/png") ||
                contentType.equals("image/jpeg") ||
                contentType.equals("image/jpg") ||
                contentType.equals("image/gif");
    }
}
