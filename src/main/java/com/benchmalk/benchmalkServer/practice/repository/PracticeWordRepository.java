package com.benchmalk.benchmalkServer.practice.repository;

import com.benchmalk.benchmalkServer.practice.domain.PracticeWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PracticeWordRepository extends JpaRepository<PracticeWord, Long> {
}
