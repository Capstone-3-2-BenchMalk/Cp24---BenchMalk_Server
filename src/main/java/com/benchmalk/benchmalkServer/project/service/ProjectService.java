package com.benchmalk.benchmalkServer.project.service;

import com.benchmalk.benchmalkServer.project.domain.Project;
import com.benchmalk.benchmalkServer.project.repository.ProjectRepository;
import com.benchmalk.benchmalkServer.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserService userService;

    public Project create(String userid, String name, Integer min_time, Integer max_time) {
        Project project = new Project(name, min_time, max_time, userService.getUserByUserId(userid));
        return projectRepository.save(project);
    }
}
