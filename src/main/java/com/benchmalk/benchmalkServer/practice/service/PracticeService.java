package com.benchmalk.benchmalkServer.practice.service;

import com.benchmalk.benchmalkServer.common.exception.CustomException;
import com.benchmalk.benchmalkServer.common.exception.ErrorCode;
import com.benchmalk.benchmalkServer.practice.domain.Practice;
import com.benchmalk.benchmalkServer.practice.repository.PracticeRepository;
import com.benchmalk.benchmalkServer.project.domain.Project;
import com.benchmalk.benchmalkServer.project.service.ProjectService;
import com.benchmalk.benchmalkServer.util.FileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PracticeService {
    private final ProjectService projectService;
    private final PracticeRepository practiceRepository;
    private final FileManager fileManager;

    public Practice create(String name, String memo, Long projectid, String userid, MultipartFile file) {
        Project project = projectService.getProject(projectid);
        if (!project.getUser().getUserid().equals(userid)) {
            throw new CustomException(ErrorCode.METHOD_NOT_ALLOWED);
        }
        fileManager.savePractice(file);
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
        if (userid == null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        if (projectid != null) {
            Project project = projectService.getProject(projectid);
            if (!project.getUser().getUserid().equals(userid)) {
                throw new CustomException(ErrorCode.METHOD_NOT_ALLOWED);
            }
            return practiceRepository.findAllByProject(project);
        }
        return projectService.getProjects(userid).stream().map(Project::getPractices).flatMap(List::stream).toList();
    }

    public Practice modify(String userid, Long practiceid, String name, String memo) {
        Practice practice = getPractice(practiceid);
        if (!practice.getProject().getUser().getUserid().equals(userid)) {
            throw new CustomException(ErrorCode.METHOD_NOT_ALLOWED);
        }
        if (!name.isBlank()) {
            practice.setName(name);
        }
        if (!memo.isBlank()) {
            practice.setMemo(memo);
        }
        return practiceRepository.save(practice);
    }
}
