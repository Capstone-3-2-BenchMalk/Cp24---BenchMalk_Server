package com.benchmalk.benchmalkServer.practice.controller;

import com.benchmalk.benchmalkServer.common.exception.CustomException;
import com.benchmalk.benchmalkServer.common.exception.ErrorCode;
import com.benchmalk.benchmalkServer.practice.domain.Practice;
import com.benchmalk.benchmalkServer.practice.dto.PracticeModifyRequest;
import com.benchmalk.benchmalkServer.practice.dto.PracticeRequest;
import com.benchmalk.benchmalkServer.practice.dto.PracticeResponse;
import com.benchmalk.benchmalkServer.practice.service.PracticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/practices")
public class PracticeController {
    private final PracticeService practiceService;

    @PostMapping
    public ResponseEntity<PracticeResponse> createPractice(@Valid @RequestPart(value = "json") PracticeRequest practiceRequest, @RequestPart MultipartFile file
            , @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (fileExtension == null || !List.of("mp3", "mp4").contains(fileExtension)) {
            throw new CustomException(ErrorCode.BAD_FILE_EXTENSION);
        }
        Practice practice = practiceService.create(practiceRequest.getName(),
                practiceRequest.getMemo(), Long.parseLong(practiceRequest.getProjectid()), userDetails.getUsername(), file);

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
    public ResponseEntity<List<PracticeResponse>> getPractices(@RequestParam(required = false) Long projectid
            , @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(practiceService.getPractices(userDetails.getUsername(), projectid).stream()
                .map(p -> new PracticeResponse(p)).toList());
    }

    @PatchMapping
    public ResponseEntity<PracticeResponse> modifyPractice(@Valid @RequestBody PracticeModifyRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(new PracticeResponse(practiceService.modify(userDetails.getUsername(), request.getPracticeid(), request.getName(), request.getMemo())));
    }
}
