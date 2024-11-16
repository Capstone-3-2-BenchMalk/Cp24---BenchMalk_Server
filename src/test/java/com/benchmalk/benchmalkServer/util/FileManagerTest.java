package com.benchmalk.benchmalkServer.util;

import com.benchmalk.benchmalkServer.model.domain.ModelType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class FileManagerTest {
    private final FileManager fileManager = new FileManager();

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
    void saveAndDeletePractice() {
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "hello".getBytes());
        String filepath = fileManager.savePractice(mockFile);
        File file = new File(filepath);
        assertThat(file.exists()).isTrue();
        assertThat(file.getName()).contains("Practice_");
        fileManager.deleteFile(file.getAbsolutePath());
        assertThat(file.exists()).isFalse();
    }

    @Test
    void saveAndDeleteProvidedModel() {
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "hello".getBytes());
        String filepath = fileManager.saveModel(mockFile, ModelType.PROVIDED);
        File file = new File(filepath);
        assertThat(file.exists()).isTrue();
        assertThat(file.getName()).contains("Model_PROVIDED");
        fileManager.deleteFile(file.getAbsolutePath());
        assertThat(file.exists()).isFalse();
    }

    @Test
    void saveAndDeleteCreatedModel() {
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "hello".getBytes());
        String filepath = fileManager.saveModel(mockFile, ModelType.CREATED);
        File file = new File(filepath);
        assertThat(file.exists()).isTrue();
        assertThat(file.getName()).doesNotContain("Model_PROVIDED");
        assertThat(file.getName()).contains("Model");
        fileManager.deleteFile(file.getAbsolutePath());
        assertThat(file.exists()).isFalse();
    }

}