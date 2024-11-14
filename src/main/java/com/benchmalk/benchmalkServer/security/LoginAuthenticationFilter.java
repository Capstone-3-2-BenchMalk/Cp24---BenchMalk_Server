package com.benchmalk.benchmalkServer.security;

import com.benchmalk.benchmalkServer.user.dto.UserLoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private ObjectMapper objectMapper = new ObjectMapper();

    public LoginAuthenticationFilter() {
        super(new AntPathRequestMatcher("/api/v1/login"));
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        String method = request.getMethod();

        if (!method.equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        UserLoginRequest userLoginRequest = objectMapper.readValue(request.getReader(), UserLoginRequest.class);

        if (!StringUtils.hasLength(userLoginRequest.getUserid())
                || !StringUtils.hasLength(userLoginRequest.getPassword())) {
            throw new IllegalArgumentException("id or password is empty");
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userLoginRequest.getUserid(),
                userLoginRequest.getPassword());

        Authentication authenticate = getAuthenticationManager().authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        return authenticate;
    }
}
