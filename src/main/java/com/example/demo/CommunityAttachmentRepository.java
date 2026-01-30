package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommunityAttachmentRepository extends JpaRepository<CommunityAttachment, Long> {

    // Get all attachments for a specific message
    List<CommunityAttachment> findByMessageId(Long messageId);
}
