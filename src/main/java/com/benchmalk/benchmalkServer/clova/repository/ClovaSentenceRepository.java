package com.benchmalk.benchmalkServer.clova.repository;

import com.benchmalk.benchmalkServer.clova.domain.ClovaSentence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClovaSentenceRepository extends JpaRepository<ClovaSentence, Long> {
}
