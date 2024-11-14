package com.benchmalk.benchmalkServer.practice.repository;

import com.benchmalk.benchmalkServer.practice.domain.PracticeAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PracticeAnalysisRepository extends JpaRepository<PracticeAnalysis, Long> {
}
