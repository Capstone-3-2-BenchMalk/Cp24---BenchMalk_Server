package com.benchmalk.benchmalkServer.practice.repository;

import com.benchmalk.benchmalkServer.practice.domain.PracticeSentence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PracticeSentenceRepository extends JpaRepository<PracticeSentence, Long> {
}
