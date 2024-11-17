package com.benchmalk.benchmalkServer.clova.repository;

import com.benchmalk.benchmalkServer.clova.domain.ClovaWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClovaWordRepository extends JpaRepository<ClovaWord, Long> {
}
