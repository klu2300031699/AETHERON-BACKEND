package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommunityService {

    @Autowired
    private CommunityMessageRepository messageRepository;

    @Autowired
    private CommunityAttachmentRepository attachmentRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    private static final String UPLOAD_DIR = "uploads/community/";

    // Get user's enrolled course communities
    public List<Map<String, Object>> getUserCommunities(Long userId) {
        List<Enrollment> enrollments = enrollmentRepository.findByUserId(userId);

        return enrollments.stream()
                .map(enrollment -> {
                    Map<String, Object> community = new HashMap<>();
                    community.put("courseId", enrollment.getCourseId());
                    community.put("courseTitle", enrollment.getCourseTitle());
                    community.put("courseCategory", enrollment.getCourseCategory());

                    // Count messages in this community
                    long messageCount = messageRepository.findByCourseIdOrderBySentAtAsc(enrollment.getCourseId())
                            .size();
                    community.put("messageCount", messageCount);

                    return community;
                })
                .collect(Collectors.toList());
    }

    // Send text message to community
    public CommunityMessage sendMessage(CommunityMessage message) {
        message.setSentAt(LocalDateTime.now());
        return messageRepository.save(message);
    }

    // Get messages for a course community
    public List<Map<String, Object>> getCourseMessages(Long courseId) {
        List<CommunityMessage> messages = messageRepository.findByCourseIdOrderBySentAtAsc(courseId);

        return messages.stream()
                .map(message -> {
                    Map<String, Object> messageData = new HashMap<>();
                    messageData.put("id", message.getId());
                    messageData.put("courseId", message.getCourseId());
                    messageData.put("courseTitle", message.getCourseTitle());
                    messageData.put("userId", message.getUserId());
                    messageData.put("userName", message.getUserName());
                    messageData.put("userEmail", message.getUserEmail());
                    messageData.put("message", message.getMessage());
                    messageData.put("messageType", message.getMessageType());
                    messageData.put("sentAt", message.getSentAt());

                    // Get attachments for this message
                    List<CommunityAttachment> attachments = attachmentRepository.findByMessageId(message.getId());
                    messageData.put("attachments", attachments);

                    return messageData;
                })
                .collect(Collectors.toList());
    }

    // Save file attachment
    public CommunityAttachment saveAttachment(Long messageId, MultipartFile file) throws IOException {
        // Create upload directory if it doesn't exist
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFilename = System.currentTimeMillis() + "_" + UUID.randomUUID().toString() + extension;

        // Save file to disk
        Path filePath = Paths.get(UPLOAD_DIR + uniqueFilename);
        Files.write(filePath, file.getBytes());

        // Create attachment record
        CommunityAttachment attachment = new CommunityAttachment();
        attachment.setMessageId(messageId);
        attachment.setFileName(originalFilename);
        attachment.setFileType(file.getContentType());
        attachment.setFileSize(file.getSize());
        attachment.setFilePath(UPLOAD_DIR + uniqueFilename);
        attachment.setUploadedAt(LocalDateTime.now());

        return attachmentRepository.save(attachment);
    }

    // Get attachment by ID
    public CommunityAttachment getAttachment(Long id) {
        return attachmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attachment not found"));
    }

    // Get all attachments for a message
    public List<CommunityAttachment> getMessageAttachments(Long messageId) {
        return attachmentRepository.findByMessageId(messageId);
    }
}
