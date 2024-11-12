package com.benchmalk.benchmalkServer;

import com.benchmalk.benchmalkServer.common.exception.CustomException;
import com.benchmalk.benchmalkServer.common.exception.ErrorCode;
import com.benchmalk.benchmalkServer.user.domain.User;
import com.benchmalk.benchmalkServer.user.dto.UserResponse;
import com.benchmalk.benchmalkServer.user.dto.UserSignupRequest;
import com.benchmalk.benchmalkServer.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MainController {
    private final UserService userService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/api/v1/auth")
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody UserSignupRequest userSignupRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        try {
            User user = userService.create(userSignupRequest.getUserid(), userSignupRequest.getUsername(), userSignupRequest.getPassword());
            return ResponseEntity.ok(new UserResponse(user));
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.USERNAME_CONFLICT);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
