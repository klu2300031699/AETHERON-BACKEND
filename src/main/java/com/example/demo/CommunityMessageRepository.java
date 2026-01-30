package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommunityMessageRepository extends JpaRepository<CommunityMessage, Long> {
    List<CommunityMessage> findByCourseIdOrderByCreatedAtAsc(Long courseId);
    List<CommunityMessage> findByUserIdOrderByCreatedAtDesc(Long userId);
    long countByCourseId(Long courseId);
}
