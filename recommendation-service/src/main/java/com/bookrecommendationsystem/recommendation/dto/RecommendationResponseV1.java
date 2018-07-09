package com.bookrecommendationsystem.recommendation.dto;

import org.springframework.http.HttpStatus;
import java.util.Set;

public class RecommendationResponseV1 {
    private Set<BookResponseV1> books;
    private HttpStatus statusCode;
    private ErrorResponseV1 error;

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

    public Set<BookResponseV1> getBooks() {
        return books;
    }
    public void setBooks(Set<BookResponseV1> books) {
        this.books = books;
    }
}
