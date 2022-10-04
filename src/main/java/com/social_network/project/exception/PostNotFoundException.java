package com.social_network.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PostNotFoundException extends RuntimeException{

    public PostNotFoundException(String message) {
        super(message);
    }
}
