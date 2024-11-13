package com.benchmalk.benchmalkServer.model.repository;

import com.benchmalk.benchmalkServer.model.domain.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {
}
