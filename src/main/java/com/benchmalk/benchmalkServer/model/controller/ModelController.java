package com.benchmalk.benchmalkServer.model.controller;

import com.benchmalk.benchmalkServer.common.exception.CustomException;
import com.benchmalk.benchmalkServer.common.exception.ErrorCode;
import com.benchmalk.benchmalkServer.model.domain.Model;
import com.benchmalk.benchmalkServer.model.dto.ModelRequest;
import com.benchmalk.benchmalkServer.model.dto.ModelResponse;
import com.benchmalk.benchmalkServer.model.service.ModelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/models")
public class ModelController {
    private final ModelService modelService;

    @PostMapping
    public ResponseEntity<ModelResponse> createModel(@Valid @RequestPart(value = "json") ModelRequest modelRequest, BindingResult bindingResult, @RequestPart MultipartFile file
            , @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        if (bindingResult.hasErrors()) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (fileExtension == null || !List.of("mp3", "mp4").contains(fileExtension)) {
            throw new CustomException(ErrorCode.BAD_FILE_EXTENSION);
        }
        Model model = modelService.create(userDetails.getUsername(), modelRequest.getName(), modelRequest.getType());

//        String uploadPath = "C:/Users/dksvl/Desktop/";
//        String originalFileName = file.getOriginalFilename();
//        String savedFileName = UUID.randomUUID().toString() + "_" + originalFileName;
//        File file1 = new File(uploadPath + savedFileName);
//        file.transferTo(file1);
        return ResponseEntity.ok(new ModelResponse(model));
    }

    @DeleteMapping("/{modelid}")
    public ResponseEntity<String> deleteModel(@PathVariable Long modelid) {
        return ResponseEntity.ok(modelService.delete(modelid).toString() + "deletion success");
    }

    @GetMapping("/{modelid}")
    public ResponseEntity<ModelResponse> getModel(@PathVariable Long modelid) {
        return ResponseEntity.ok(new ModelResponse(modelService.getModel(modelid)));
    }

    @GetMapping
    public ResponseEntity<List<ModelResponse>> getModels(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(modelService.getModels(userDetails.getUsername()).stream().map(ModelResponse::new).toList());
    }
}
