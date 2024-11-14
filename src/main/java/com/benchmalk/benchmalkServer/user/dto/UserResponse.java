package com.benchmalk.benchmalkServer.user.dto;

import com.benchmalk.benchmalkServer.user.domain.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponse {

    private Long id;
    private String userid;
    private String username;

    public UserResponse(User user) {
        this.id = user.getId();
        this.userid = user.getUserid();
        this.username = user.getUsername();
    }
}
