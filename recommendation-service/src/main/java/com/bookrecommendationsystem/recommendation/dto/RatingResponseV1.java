package com.bookrecommendationsystem.recommendation.dto;

import org.springframework.http.HttpStatus;

public class RatingResponseV1 {
    private String asin;
    private String username;
    private String ratingLevel;
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

    public String getAsin() {
        return asin;
    }
    public void setAsin(String asin) {
        this.asin = asin;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getRatingLevel() {
        return ratingLevel;
    }
    public void setRatingLevel(String ratingLevel) {
        this.ratingLevel = ratingLevel;
    }
}
