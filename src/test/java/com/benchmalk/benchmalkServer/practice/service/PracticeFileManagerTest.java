package com.benchmalk.benchmalkServer.practice.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class PracticeFileManagerTest {
    private final PracticeFileManager practiceFileManager = new PracticeFileManager();

    @BeforeAll
    static void init() {
        String home = System.getProperty("user.home");
        String uploadPath = home + "/benchmalk/files";
        File file = new File(uploadPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    @Test
    void saveAndDeleteFile() {
        String filepath = practiceFileManager.saveFile(new MockMultipartFile("file", "test.txt", "text/plain", "hello".getBytes()));
        File file = new File(filepath);
        assertThat(file.exists()).isTrue();
        practiceFileManager.deleteFile(file.getAbsolutePath());
        assertThat(file.exists()).isFalse();
    }
}