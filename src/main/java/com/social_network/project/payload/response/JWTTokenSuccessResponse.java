package com.social_network.project.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JWTTokenSuccessResponse {
    private boolean success;
    private String token;
}