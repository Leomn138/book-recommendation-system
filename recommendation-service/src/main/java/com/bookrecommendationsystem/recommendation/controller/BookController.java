package com.bookrecommendationsystem.recommendation.controller;

import com.bookrecommendationsystem.recommendation.dto.BookResponseV1;
import com.bookrecommendationsystem.recommendation.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

@RestController
public class BookController {

	@Autowired
	private BookService bookService;
	private final Logger log = LoggerFactory.getLogger(getClass());
	@RequestMapping(path = "/v1/books/{asin}", method = RequestMethod.GET)
	public ResponseEntity<BookResponseV1> getBookByAsinNumber(@PathVariable String asin) {
		log.info("BookController - Started GetBookByAsinNumber asin: " + asin + ". " + new Date());
		try {
			BookResponseV1 book = bookService.findByAsin(asin);
			log.info("BookController - Finished GetBookByAsinNumber asin: " + asin + ". " + new Date());
			return new ResponseEntity<>(book, book.getStatusCode());
		} catch (Exception e) {
			log.error("BookController - Error GetBookByAsinNumber asin: " + asin + ". " + new Date(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(path = "/v1/books", method = RequestMethod.GET)
	public ResponseEntity<List<BookResponseV1>> getAllBooks() {
		log.info("BookController - Started GetAllBooks. " + new Date());
		try {
			List<BookResponseV1> books = bookService.getAll();
			log.info("BookController - Finished GetAllBooks. " + new Date());
			return new ResponseEntity<>(books, HttpStatus.OK);
		} catch (Exception e) {
			log.error("BookController - Error GetBookByAsinNumber. " + new Date(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
