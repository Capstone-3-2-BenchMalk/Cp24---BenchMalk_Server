package com.benchmalk.benchmalkServer.clova.repository;

import com.benchmalk.benchmalkServer.clova.domain.ClovaAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClovaAnalysisRepository extends JpaRepository<ClovaAnalysis, Long> {
}
