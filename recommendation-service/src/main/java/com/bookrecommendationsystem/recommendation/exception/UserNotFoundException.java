package com.bookrecommendationsystem.recommendation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Specified user was not found in the system")
public class UserNotFoundException extends Exception {
    private static final long serialVersionUID = 100L;
}

