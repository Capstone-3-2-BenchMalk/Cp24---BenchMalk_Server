package com.benchmalk.benchmalkServer.practice.service;

import com.benchmalk.benchmalkServer.common.exception.CustomException;
import com.benchmalk.benchmalkServer.common.exception.ErrorCode;
import com.benchmalk.benchmalkServer.practice.domain.Practice;
import com.benchmalk.benchmalkServer.practice.repository.PracticeRepository;
import com.benchmalk.benchmalkServer.project.domain.Project;
import com.benchmalk.benchmalkServer.project.service.ProjectService;
import com.benchmalk.benchmalkServer.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PracticeService {
    private final UserService userService;
    private final ProjectService projectService;
    private final PracticeRepository practiceRepository;

    public Practice create(String name, String memo, Long projectid) {
        Project project = projectService.getProject(projectid);
        Practice practice = new Practice(name, memo, project);
        return practiceRepository.save(practice);
    }

    public Long delete(Long practiceid) {
        practiceRepository.deleteById(practiceid);
        return practiceid;
    }

    public Practice getPractice(Long practiceid) {
        return practiceRepository.findById(practiceid)
                .orElseThrow(() -> new CustomException(ErrorCode.PRACTICE_NOT_FOUND));
    }

    public List<Practice> getPractices(String userid, Long projectid) {
        if (projectid != null) {
            return practiceRepository.findAllByProjectId(projectid);
        }
        if (userid == null) {
            return projectService.getProjects(userid).stream().map(Project::getPractices).flatMap(List::stream).toList();
        }
        return practiceRepository.findAll();
    }
}