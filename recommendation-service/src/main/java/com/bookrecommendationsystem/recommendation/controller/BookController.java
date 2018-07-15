package com.bookrecommendationsystem.recommendation.controller;

import com.bookrecommendationsystem.recommendation.dto.BookResponseV1;
import com.bookrecommendationsystem.recommendation.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class BookController {

	@Autowired
	private BookService bookService;
	private final Logger log = LoggerFactory.getLogger(getClass());
	@RequestMapping(path = "/v1/books/{asin}", method = RequestMethod.GET)
	public ResponseEntity<BookResponseV1> getBookByAsinNumber(@PathVariable String asin) {
		log.info("Started GetBookByAsinNumber asin: " + asin + ".");
		try {
			BookResponseV1 book = bookService.findByAsin(asin);
			log.info("Finished GetBookByAsinNumber asin: " + asin + ".");
			return new ResponseEntity<>(book, book.getStatusCode());
		} catch (Exception e) {
			log.error("Error GetBookByAsinNumber asin: " + asin + ".", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(path = "/v1/books", method = RequestMethod.GET)
	public ResponseEntity<List<BookResponseV1>> getAllBooks() {
		log.info("Started GetAllBooks. ");
		try {
			List<BookResponseV1> books = bookService.getAll();
			log.info("Finished GetAllBooks.");
			return new ResponseEntity<>(books, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error GetBookByAsinNumber.", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
