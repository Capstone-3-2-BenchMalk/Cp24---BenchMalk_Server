package com.benchmalk.benchmalkServer.model.service;

import com.benchmalk.benchmalkServer.common.exception.CustomException;
import com.benchmalk.benchmalkServer.common.exception.ErrorCode;
import com.benchmalk.benchmalkServer.model.domain.Model;
import com.benchmalk.benchmalkServer.model.domain.ModelType;
import com.benchmalk.benchmalkServer.model.repository.ModelRepository;
import com.benchmalk.benchmalkServer.user.domain.User;
import com.benchmalk.benchmalkServer.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ModelService {
    private final ModelRepository modelRepository;
    private final UserService userService;

    public Model create(String userid, String name, ModelType type) {
        if (type == ModelType.CREATED) {
            User user = userService.getUserByUserId(userid);
            if (modelRepository.existsByNameAndUser(name, user)) {
                throw new CustomException(ErrorCode.MODEL_CONFLICT);
            }
            Model model = new Model(name, type, user);
            return modelRepository.save(model);
        }
        if (type == ModelType.PROVIDED) {
            if (modelRepository.existsByNameAndModelType(name, type)) {
                throw new CustomException(ErrorCode.MODEL_CONFLICT);
            }
            Model model = new Model(name, ModelType.PROVIDED);
            return modelRepository.save(model);
        }
        throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    public Long delete(Long modelid) {
        modelRepository.deleteById(modelid);
        return modelid;
    }

    public Model getModel(Long modelid) {
        return modelRepository.findById(modelid)
                .orElseThrow(() -> new CustomException(ErrorCode.MODEL_NOT_FOUND));
    }

    public List<Model> getModels(String userid) {
        List<Model> models = new ArrayList<>();
        models.addAll(modelRepository.findByModelType(ModelType.PROVIDED));
        if (!userid.isBlank()) {
            models.addAll(modelRepository.findByUser(userService.getUserByUserId(userid)));
        }
        return models;
    }
}
