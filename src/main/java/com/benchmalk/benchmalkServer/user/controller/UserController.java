package com.benchmalk.benchmalkServer.user.controller;


import com.benchmalk.benchmalkServer.user.dto.UserResponse;
import com.benchmalk.benchmalkServer.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private final UserService userService;

    @GetMapping("/{userid}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("userid") String userid) {
        return ResponseEntity.ok(new UserResponse(userService.getUserByUserId(userid)));
    }
}
