package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CourseDetailRepository extends JpaRepository<CourseDetail, Long> {
    Optional<CourseDetail> findByCourseId(Long courseId);
    boolean existsByCourseId(Long courseId);
    void deleteByCourseId(Long courseId);
}
