package com.bookrecommendationsystem.recommendation.dto;

import org.springframework.http.HttpStatus;

public class BookResponseV1 {
    private String asin;
    private String title;
    private String author;
    private String genre;
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

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
}
