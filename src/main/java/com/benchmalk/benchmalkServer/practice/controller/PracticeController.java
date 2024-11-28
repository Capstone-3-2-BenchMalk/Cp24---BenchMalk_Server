package com.benchmalk.benchmalkServer.practice.controller;

import com.benchmalk.benchmalkServer.clova.service.ClovaService;
import com.benchmalk.benchmalkServer.practice.domain.Practice;
import com.benchmalk.benchmalkServer.practice.dto.PracticeModifyRequest;
import com.benchmalk.benchmalkServer.practice.dto.PracticeRequest;
import com.benchmalk.benchmalkServer.practice.dto.PracticeResponse;
import com.benchmalk.benchmalkServer.practice.service.PracticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/practices")
public class PracticeController {
    private final PracticeService practiceService;
    private final ClovaService clovaService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<PracticeResponse> createPractice(@Valid @RequestPart(value = "json") PracticeRequest practiceRequest, @RequestPart MultipartFile file
            , @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        Practice practice = practiceService.create(practiceRequest.getName(),
                practiceRequest.getMemo(), Long.parseLong(practiceRequest.getProjectid()), userDetails.getUsername(), file);

        return ResponseEntity.ok(new PracticeResponse(practice));
    }

    @PostMapping(value = "/test", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<PracticeResponse> createTestPractice(@Valid @RequestPart(value = "json") PracticeRequest practiceRequest, @RequestPart MultipartFile file
            , @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        Practice practice = practiceService.createTest(practiceRequest.getName(),
                practiceRequest.getMemo(), Long.parseLong(practiceRequest.getProjectid()), userDetails.getUsername(), file);

        return ResponseEntity.ok(new PracticeResponse(practice));
    }

    @DeleteMapping("/{practiceid}")
    public ResponseEntity<String> deletePractice(@PathVariable("practiceid") Long practiceid) {
        return ResponseEntity.ok("Practice Id = " + practiceService.delete(practiceid).toString() + " deletion success");
    }

    @GetMapping("/{practiceid}")
    public ResponseEntity<PracticeResponse> getPractice(@PathVariable("practiceid") Long practiceid) {
        Practice practice = practiceService.getPractice(practiceid);
        Map<String, Float> achievement = clovaService.calculateAchievement(practice.getClovaAnalysis(), practice.getProject().getModel().getClovaAnalysis());
        return ResponseEntity.ok(new PracticeResponse(practice, achievement));
    }

    @GetMapping
    public ResponseEntity<List<PracticeResponse>> getPractices(@RequestParam(required = false) Long projectid
            , @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(practiceService.getPractices(userDetails.getUsername(), projectid).stream()
                .map(p -> {
                    Map<String, Float> achievement = clovaService.calculateAchievement(p.getClovaAnalysis(), p.getProject().getModel().getClovaAnalysis());
                    return new PracticeResponse(p, achievement);
                }).toList());
    }

    @PatchMapping
    public ResponseEntity<PracticeResponse> modifyPractice(@Valid @RequestBody PracticeModifyRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        Practice practice = practiceService.modify(userDetails.getUsername(), request.getPracticeid(), request.getName(), request.getMemo());
        Map<String, Float> achievement = clovaService.calculateAchievement(practice.getClovaAnalysis(), practice.getProject().getModel().getClovaAnalysis());
        return ResponseEntity.ok(new PracticeResponse(practice, achievement));
    }
}
