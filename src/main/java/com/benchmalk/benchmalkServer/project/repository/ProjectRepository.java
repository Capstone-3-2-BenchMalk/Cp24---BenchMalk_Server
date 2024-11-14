package com.benchmalk.benchmalkServer.project.repository;

import com.benchmalk.benchmalkServer.project.domain.Project;
import com.benchmalk.benchmalkServer.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Boolean existsByNameAndUser(String name, User user);

    List<Project> findByUser(User user);

}
