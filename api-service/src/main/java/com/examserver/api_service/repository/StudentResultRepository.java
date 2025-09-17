package com.examserver.api_service.repository;

import com.examserver.api_service.entity.StudentResult;
import com.examserver.api_service.entity.StudentResultId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StudentResultRepository extends JpaRepository<StudentResult, StudentResultId> {
    List<StudentResult> findByExamYear(Integer examYear);
}

