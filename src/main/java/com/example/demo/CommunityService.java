package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommunityService {
    
    @Autowired
    private CommunityMessageRepository messageRepository;
    
    @Autowired
    private CommunityAttachmentRepository attachmentRepository;
    
    // Get all messages for a course
    public List<CommunityMessage> getMessagesByCourseId(Long courseId) {
        return messageRepository.findByCourseIdOrderByCreatedAtAsc(courseId);
    }
    
    // Get a single message by ID
    public CommunityMessage getMessageById(Long messageId) {
        return messageRepository.findById(messageId).orElse(null);
    }
    
    // Get message count for a course
    public long getMessageCountByCourseId(Long courseId) {
        return messageRepository.countByCourseId(courseId);
    }
    
    // Create a new message
    @Transactional
    public CommunityMessage createMessage(CommunityMessage message) {
        return messageRepository.save(message);
    }
    
    // Delete a message
    @Transactional
    public void deleteMessage(Long messageId) {
        // Delete associated attachments first
        List<CommunityAttachment> attachments = attachmentRepository.findByMessageId(messageId);
        attachmentRepository.deleteAll(attachments);
        
        // Delete the message
        messageRepository.deleteById(messageId);
    }
    
    // Get attachments for a message
    public List<CommunityAttachment> getAttachmentsByMessageId(Long messageId) {
        return attachmentRepository.findByMessageId(messageId);
    }
    
    // Add attachment to a message
    @Transactional
    public CommunityAttachment addAttachment(CommunityAttachment attachment) {
        return attachmentRepository.save(attachment);
    }
}
