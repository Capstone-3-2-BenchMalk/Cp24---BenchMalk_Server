package com.benchmalk.benchmalkServer.practice.service;

import com.benchmalk.benchmalkServer.clova.domain.ClovaAnalysis;
import com.benchmalk.benchmalkServer.clova.dto.ClovaResponse;
import com.benchmalk.benchmalkServer.clova.repository.ClovaAnalysisRepository;
import com.benchmalk.benchmalkServer.clova.service.ClovaService;
import com.benchmalk.benchmalkServer.common.exception.CustomException;
import com.benchmalk.benchmalkServer.common.exception.ErrorCode;
import com.benchmalk.benchmalkServer.practice.domain.Practice;
import com.benchmalk.benchmalkServer.practice.domain.PracticeStatus;
import com.benchmalk.benchmalkServer.practice.repository.PracticeRepository;
import com.benchmalk.benchmalkServer.project.domain.Project;
import com.benchmalk.benchmalkServer.project.service.ProjectService;
import com.benchmalk.benchmalkServer.util.AudioAnalyzer;
import com.benchmalk.benchmalkServer.util.FileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PracticeService {
    private final ProjectService projectService;
    private final PracticeRepository practiceRepository;
    private final FileManager fileManager;
    private final ClovaService clovaService;
    private final AudioAnalyzer audioAnalyzer;
    private final ClovaAnalysisRepository clovaAnalysisRepository;

    public Practice create(String name, String memo, Long projectid, String userid, MultipartFile file) {
        Project project = projectService.getProject(projectid);
        if (!project.getUser().getUserid().equals(userid)) {
            throw new CustomException(ErrorCode.METHOD_NOT_ALLOWED);
        }
        String filePath = fileManager.savePractice(file);
        Practice practice = new Practice(name, memo, project);
        practiceRepository.save(practice);
        try {
            clovaService.callClova(filePath).subscribe(m -> setPracticeAnalysis(practice.getId(), m, filePath));
        } catch (WebClientResponseException e) {
            System.out.println(e.getResponseBodyAsString());
            System.out.println(e.getStatusCode() + e.getStatusText());
            practice.setStatus(PracticeStatus.FAILED);
            practiceRepository.save(practice);
            throw new CustomException(ErrorCode.API_CALL_ERROR);
        }
        return practice;
    }

    public Practice createTest(String name, String memo, Long projectid, String userid, MultipartFile file) {
        Project project = projectService.getProject(projectid);
        if (!project.getUser().getUserid().equals(userid)) {
            throw new CustomException(ErrorCode.METHOD_NOT_ALLOWED);
        }
        String filePath = fileManager.savePractice(file);
        Practice practice = new Practice(name, memo, project);
        practice.setDuration(audioAnalyzer.getDuration(filePath));
        ClovaAnalysis analysis = new ClovaAnalysis();
        analysis.setConfidence(0.6);
        analysis.setRest(10);
        analysis.setWpm(120);
        analysis.setPitch(140);
        practice.setClovaAnalysis(analysis);
        clovaAnalysisRepository.save(analysis);
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
        if (name != null) {
            practice.setName(name);
        }
        if (memo != null) {
            practice.setMemo(memo);
        }
        return practiceRepository.save(practice);
    }

    public void setPracticeAnalysis(Long practiceid, ClovaResponse clovaResponse, String filePath) {
        Practice practice = getPractice(practiceid);
        try {
            Long duration = audioAnalyzer.getDuration(filePath);
            practice.setDuration(duration);
            ClovaAnalysis analysis = clovaService.createAnalysis(clovaResponse, filePath, duration);
            practice.setClovaAnalysis(analysis);
            practice.setStatus(PracticeStatus.ANALYZED);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
            practice.setStatus(PracticeStatus.FAILED);
            practiceRepository.save(practice);
            fileManager.deleteFile(filePath);
            throw new CustomException(ErrorCode.ANALYSIS_ERROR);
        }
        practiceRepository.save(practice);
        fileManager.deleteFile(filePath);
    }
}
