package com.benchmalk.benchmalkServer;

import com.benchmalk.benchmalkServer.common.exception.ErrorCode;
import com.benchmalk.benchmalkServer.user.dto.UserSignupRequest;
import com.benchmalk.benchmalkServer.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MainController {
    @Autowired
    UserService userService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/api/v1/auth")
    public Object signup(@Valid @RequestBody UserSignupRequest userSignupRequest) {
        try{
            userService.create(userSignupRequest.getUsername(),userSignupRequest.getPassword());
        }catch (DataIntegrityViolationException e){
            e.printStackTrace();
            return ResponseEntity.status(ErrorCode.USERNAME_CONFLICT.getStatus()).body(ErrorCode.USERNAME_CONFLICT.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus()).body(ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        }

        return ResponseEntity.ok();
    }
}
