package com.benchmalk.benchmalkServer.project.controller;

import com.benchmalk.benchmalkServer.project.domain.Project;
import com.benchmalk.benchmalkServer.project.dto.ProjectModifyRequest;
import com.benchmalk.benchmalkServer.project.dto.ProjectRequest;
import com.benchmalk.benchmalkServer.project.dto.ProjectResponse;
import com.benchmalk.benchmalkServer.project.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/projects")
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody ProjectRequest projectRequest, @AuthenticationPrincipal UserDetails userDetails) {
        Project project = projectService.create(userDetails.getUsername(), projectRequest.getName(),
                projectRequest.getMin_time(), projectRequest.getMax_time());
        return ResponseEntity.ok(new ProjectResponse(project));
    }

    @DeleteMapping("/{projectid}")
    public ResponseEntity<String> deleteProject(@PathVariable Long projectid) {
        return ResponseEntity.ok("Project Id = " + projectService.delete(projectid).toString() + " deletion success");
    }

    @GetMapping("/{projectid}")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable Long projectid) {
        return ResponseEntity.ok(new ProjectResponse(projectService.getProject(projectid)));
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getProjects(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                projectService.getProjects(userDetails.getUsername()).stream().map(p -> new ProjectResponse(p)).toList()
        );
    }

    @PatchMapping
    public ResponseEntity<ProjectResponse> modifyProject(@Valid @RequestBody ProjectModifyRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(new ProjectResponse(projectService.modify(userDetails.getUsername(), request.getProjectid()
                , request.getName(), request.getMin_time(), request.getMax_time(), request.getModelid())));
    }


}
