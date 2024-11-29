package com.benchmalk.benchmalkServer.model.service;

import com.benchmalk.benchmalkServer.clova.domain.ClovaAnalysis;
import com.benchmalk.benchmalkServer.clova.dto.ClovaResponse;
import com.benchmalk.benchmalkServer.clova.service.ClovaService;
import com.benchmalk.benchmalkServer.common.exception.CustomException;
import com.benchmalk.benchmalkServer.common.exception.ErrorCode;
import com.benchmalk.benchmalkServer.model.domain.Model;
import com.benchmalk.benchmalkServer.model.domain.ModelType;
import com.benchmalk.benchmalkServer.model.repository.ModelRepository;
import com.benchmalk.benchmalkServer.project.domain.Project;
import com.benchmalk.benchmalkServer.project.repository.ProjectRepository;
import com.benchmalk.benchmalkServer.user.domain.User;
import com.benchmalk.benchmalkServer.user.service.UserService;
import com.benchmalk.benchmalkServer.util.AudioAnalyzer;
import com.benchmalk.benchmalkServer.util.FileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ModelService {
    private final ModelRepository modelRepository;
    private final UserService userService;
    private final FileManager fileManager;
    private final ClovaService clovaService;
    private final AudioAnalyzer audioAnalyzer;
    private final ProjectRepository projectRepository;

    public Model create(String userid, String name, String description, ModelType type, MultipartFile file) {
        if (type == ModelType.CREATED) {
            User user = userService.getUserByUserId(userid);
            if (modelRepository.existsByNameAndUser(name, user)) {
                throw new CustomException(ErrorCode.MODEL_CONFLICT);
            }
            String filepath = fileManager.saveModel(file, type);
            Model model = new Model(name, description, type, user, filepath);
            modelRepository.save(model);
            clovaService.callClova(filepath).subscribe(m -> setModelAnalysis(model.getId(), m, filepath));
            model.setDuration(audioAnalyzer.getDuration(filepath));
            return model;
        }
        if (type == ModelType.PROVIDED) {
            if (modelRepository.existsByNameAndModelType(name, type)) {
                throw new CustomException(ErrorCode.MODEL_CONFLICT);
            }
            String filepath = fileManager.saveModel(file, type);
            Model model = new Model(name, description, type, filepath);
            modelRepository.save(model);
            clovaService.callClova(filepath).subscribe(m -> setModelAnalysis(model.getId(), m, filepath));
            return model;
        }
        throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    public Long delete(Long modelid) {
        List<Project> projects = projectRepository.findProjectsByModelId(modelid);
        projects.forEach(p -> p.setModel(null));
        fileManager.deleteFile(getModel(modelid).getFilepath());
        modelRepository.deleteById(modelid);
        return modelid;
    }

    public Model getModel(Long modelid) {
        return modelRepository.findById(modelid)
                .orElseThrow(() -> new CustomException(ErrorCode.MODEL_NOT_FOUND));
    }

    public Resource getModelFIle(Long modelid) {
        String filePath = getModel(modelid).getFilepath();
        File file = new File(filePath);
        if (!file.exists()) {
            throw new CustomException(ErrorCode.FILE_NOT_FOUND);
        }
        return new FileSystemResource(file);
    }

    public List<Model> getModels(String userid) {
        List<Model> models = new ArrayList<>();
        models.addAll(modelRepository.findByModelType(ModelType.PROVIDED));
        if (!userid.isBlank()) {
            models.addAll(modelRepository.findByUser(userService.getUserByUserId(userid)));
        }
        return models;
    }

    public void setModelAnalysis(Long modelid, ClovaResponse clovaResponse, String filepath) {
        Model model = getModel(modelid);
        model.setDuration(audioAnalyzer.getDuration(filepath));
        ClovaAnalysis analysis = clovaService.createAnalysis(clovaResponse, filepath);
        model.setClovaAnalysis(analysis);
        modelRepository.save(model);
    }
}
