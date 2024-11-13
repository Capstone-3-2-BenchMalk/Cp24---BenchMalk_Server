package com.benchmalk.benchmalkServer.practice.controller;

import com.benchmalk.benchmalkServer.common.exception.CustomException;
import com.benchmalk.benchmalkServer.common.exception.ErrorCode;
import com.benchmalk.benchmalkServer.practice.domain.Practice;
import com.benchmalk.benchmalkServer.practice.dto.PracticeRequest;
import com.benchmalk.benchmalkServer.practice.dto.PracticeResponse;
import com.benchmalk.benchmalkServer.practice.service.PracticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/practices")
public class PracticeController {
    private final PracticeService practiceService;

    @PostMapping
    public ResponseEntity<PracticeResponse> createPractice(@Valid @RequestPart(value = "json") PracticeRequest practiceRequest, BindingResult bindingResult, @RequestPart MultipartFile file) throws IOException {
        if (bindingResult.hasErrors()) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        Practice practice = practiceService.create(practiceRequest.getName(),
                practiceRequest.getMemo(), practiceRequest.getProjectid());

        String uploadPath = "C:/Users/dksvl/Desktop/";
        String originalFileName = file.getOriginalFilename();
        String savedFileName = UUID.randomUUID().toString() + "_" + originalFileName;
        File file1 = new File(uploadPath + savedFileName);
        file.transferTo(file1);
        return ResponseEntity.ok(new PracticeResponse(practice));
    }

    @DeleteMapping("/{practiceid}")
    public ResponseEntity<String> deletePractice(@PathVariable("practiceid") Long practiceid) {
        return ResponseEntity.ok("Practice Id = " + practiceService.delete(practiceid).toString() + " deletion success");
    }

    @GetMapping("/{practiceid}")
    public ResponseEntity<PracticeResponse> getPractice(@PathVariable("practiceid") Long practiceid) {
        return ResponseEntity.ok(new PracticeResponse(practiceService.getPractice(practiceid)));
    }

    @GetMapping
    public ResponseEntity<List<PracticeResponse>> getPractices(@RequestParam(required = false) String userid, @RequestParam(required = false) Long projectid) {
        return ResponseEntity.ok(practiceService.getPractices(userid, projectid).stream()
                .map(p -> new PracticeResponse(p)).toList());
    }
}