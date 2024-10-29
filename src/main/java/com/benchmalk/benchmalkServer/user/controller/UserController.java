package com.benchmalk.benchmalkServer.user.controller;


import com.benchmalk.benchmalkServer.common.exception.ErrorCode;
import com.benchmalk.benchmalkServer.user.dto.UserLoginRequest;
import com.benchmalk.benchmalkServer.user.dto.UserLoginResponse;
import com.benchmalk.benchmalkServer.user.dto.UserSignupRequest;
import com.benchmalk.benchmalkServer.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping()
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
