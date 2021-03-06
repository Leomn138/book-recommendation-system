package com.bookrecommendationsystem.recommendation.dto;

import org.springframework.http.HttpStatus;

public class UserResponseV1 {
    private String username;
    private String name;
    private ErrorResponseV1 error;
    private HttpStatus statusCode;

    public HttpStatus getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public ErrorResponseV1 getError() {
        return error;
    }
    public void setError(ErrorResponseV1 error) {
        this.error = error;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
