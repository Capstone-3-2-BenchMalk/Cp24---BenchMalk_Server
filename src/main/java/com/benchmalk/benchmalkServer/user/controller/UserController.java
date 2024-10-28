package com.benchmalk.benchmalkServer.user.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/users")
public class UserController {

    @RequestMapping("/auth")
    public Object auth(){
        return ResponseEntity.ok();
    }
    @RequestMapping("/login")
    public Object login(){
        return ResponseEntity.ok();
    }
}
