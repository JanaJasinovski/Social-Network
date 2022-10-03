package com.social_network.project.payload.response;

import lombok.Getter;

@Getter
public class InvalidLoginResponse {
    private final String username;
    private final String password;

    public InvalidLoginResponse() {
        this.username = "Invalid Username";
        this.password = "invalid Password";
    }
}
