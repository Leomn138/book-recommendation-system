package com.bookrecommendationsystem.recommendation.controller;

import com.bookrecommendationsystem.recommendation.domain.Book;
import com.bookrecommendationsystem.recommendation.dto.BookResponseV1;
import com.bookrecommendationsystem.recommendation.exception.BookNotFoundException;
import com.bookrecommendationsystem.recommendation.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.hateoas.jaxrs.JaxRsLinkBuilder.linkTo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class BookController {

	@Autowired
	private BookService bookService;

	@RequestMapping(path = "/v1/books/{asin}", method = RequestMethod.GET)
	public ResponseEntity<BookResponseV1> getBookByAsinNumber(@PathVariable String asin) {
		try {
			BookResponseV1 book = bookService.findByAsin(asin);
			return new ResponseEntity<>(book, HttpStatus.OK);
		} catch (BookNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(path = "/v1/books", method = RequestMethod.GET)
	public ResponseEntity<List<BookResponseV1>> getAllBooks() {
		try {
			List<BookResponseV1> books = bookService.getAll();
			return new ResponseEntity<>(books, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
