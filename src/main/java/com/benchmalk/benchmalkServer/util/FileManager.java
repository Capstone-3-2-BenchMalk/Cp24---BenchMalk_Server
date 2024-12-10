package com.benchmalk.benchmalkServer.util;


import com.benchmalk.benchmalkServer.common.exception.CustomException;
import com.benchmalk.benchmalkServer.common.exception.ErrorCode;
import com.benchmalk.benchmalkServer.model.domain.ModelType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FileManager {

    @PostConstruct
    public void init() {
        String home = System.getProperty("user.home");
        String uploadPath = home + "/benchmalk/files";
        File file = new File(uploadPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public String saveModel(MultipartFile file, ModelType modelType) {
        if (modelType == ModelType.PROVIDED) {
            String savedFileName = "Model_PROVIDED_" + UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            return saveFile(file, savedFileName);
        }
        if (modelType == ModelType.CREATED) {
            String savedFileName = "Model_CREATED" + UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            return saveFile(file, savedFileName);
        }
        throw new CustomException(ErrorCode.BAD_REQUEST);
    }

    public String savePractice(MultipartFile file) {
        String savedFileName = "Practice_" + UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        return saveFile(file, savedFileName);
    }

    private String saveFile(MultipartFile file, String filename) {
        String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (fileExtension == null || !List.of("mp3").contains(fileExtension)) {
            throw new CustomException(ErrorCode.BAD_FILE_EXTENSION);
        }
        try {
            String home = System.getProperty("user.home");
            String uploadPath = home + "/benchmalk/files/";
            File file1 = new File(uploadPath + filename);
            file.transferTo(file1.getCanonicalFile());
            return file1.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.FILE_TRANSFER_ERROR);
        }
    }

    public void deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            if (!file.delete()) {
                throw new CustomException(ErrorCode.FILE_DELETE_ERROR);
            }
        }
    }
}
