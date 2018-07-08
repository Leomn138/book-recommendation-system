package com.bookrecommendationsystem.recommendation.controller;

import com.bookrecommendationsystem.recommendation.exception.BookNotFoundException;
import com.bookrecommendationsystem.recommendation.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ErrorHandler {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public void processValidationError(IllegalArgumentException e) {
		log.info("Bad Request", e);
	}

	@ExceptionHandler(BookNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public void processValidationError(BookNotFoundException e) {
		log.info("Not Found", e);
	}

	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public void processValidationError(UserNotFoundException e) {
		log.info("Not Found", e);
	}
}
