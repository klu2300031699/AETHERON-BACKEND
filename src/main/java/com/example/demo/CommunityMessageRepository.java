package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommunityMessageRepository extends JpaRepository<CommunityMessage, Long> {

    // Get all messages for a course ordered by time
    List<CommunityMessage> findByCourseIdOrderBySentAtAsc(Long courseId);

    // Get latest N messages for a course
    List<CommunityMessage> findTop50ByCourseIdOrderBySentAtDesc(Long courseId);
}
