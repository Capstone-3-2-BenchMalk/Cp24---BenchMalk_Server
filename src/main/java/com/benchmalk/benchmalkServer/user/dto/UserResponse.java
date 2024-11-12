package com.benchmalk.benchmalkServer.user.dto;

import com.benchmalk.benchmalkServer.user.domain.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponse {

    private Long id;
    private String Userid;
    private String Username;

    public UserResponse(User user) {
        this.id = user.getId();
        this.Userid = user.getUserid();
        this.Username = user.getUsername();
    }
}
