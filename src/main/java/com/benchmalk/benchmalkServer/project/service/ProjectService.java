package com.benchmalk.benchmalkServer.project.service;

import com.benchmalk.benchmalkServer.common.exception.CustomException;
import com.benchmalk.benchmalkServer.common.exception.ErrorCode;
import com.benchmalk.benchmalkServer.project.domain.Project;
import com.benchmalk.benchmalkServer.project.repository.ProjectRepository;
import com.benchmalk.benchmalkServer.user.domain.User;
import com.benchmalk.benchmalkServer.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserService userService;

    public Project create(String userid, String name, Integer min_time, Integer max_time) {
        User user = userService.getUserByUserId(userid);
        if (!projectRepository.findByNameAndUser(name, user).isEmpty()) {
            throw new CustomException(ErrorCode.PROJECT_CONFLICT);
        }
        Project project = new Project(name, min_time, max_time, user);
        return projectRepository.save(project);
    }

    public Long delete(Long projectid) {
        projectRepository.deleteById(projectid);
        return projectid;
    }

    public Project getProject(Long projectid) {
        return projectRepository.findById(projectid).orElseThrow(
                () -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));
    }

    public List<Project> getProjects(String userid) {
        if (userid.isBlank()) {
            return projectRepository.findAll();
        }
        return projectRepository.findByUser(userService.getUserByUserId(userid));
    }
}
