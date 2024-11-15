package com.benchmalk.benchmalkServer.practice.service;


import com.benchmalk.benchmalkServer.common.exception.CustomException;
import com.benchmalk.benchmalkServer.common.exception.ErrorCode;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PracticeFileManager {

    @PostConstruct
    public void init() {
        String home = System.getProperty("user.home");
        String uploadPath = home + "/benchmalk/files";
        File file = new File(uploadPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public String saveFile(MultipartFile file) {
        try {
            String home = System.getProperty("user.home");
            String uploadPath = home + "/benchmalk/files/";
            String originalFileName = file.getOriginalFilename();
            String savedFileName = UUID.randomUUID().toString() + "_" + originalFileName;
            File file1 = new File(uploadPath + savedFileName);
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
