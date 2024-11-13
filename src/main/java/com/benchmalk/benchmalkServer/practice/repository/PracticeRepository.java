package com.benchmalk.benchmalkServer.practice.repository;

import com.benchmalk.benchmalkServer.practice.domain.Practice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PracticeRepository extends JpaRepository<Practice, Long> {
    List<Practice> findAllByProjectId(Long projectId);
}
