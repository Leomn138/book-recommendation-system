package com.bookrecommendationsystem.recommendation.service;

import com.bookrecommendationsystem.recommendation.domain.Book;
import com.bookrecommendationsystem.recommendation.dto.BookResponseV1;
import com.bookrecommendationsystem.recommendation.dto.ErrorResponseV1;
import com.bookrecommendationsystem.recommendation.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository repository;

	@Override
	public BookResponseV1 findByAsin(String asin) {
		Book book = repository.findByAsin(asin);
		if (book == null)
			return fillBookResponseWithBookNotFoundError(asin);
		return mapBookToBookResponseV1(book);
	}

	@Override
	public List<BookResponseV1> getAll() {
		return new ArrayList<BookResponseV1>((Collection)repository.findAll());
	}

	private BookResponseV1 fillBookResponseWithBookNotFoundError(String asin) {
		ErrorResponseV1 error = new ErrorResponseV1();
		error.setMessage("Book asin: " + asin + " not found.");
		BookResponseV1 bookResponse = new BookResponseV1();
		bookResponse.setStatusCode(HttpStatus.NOT_FOUND);
		bookResponse.setError(error);
		return bookResponse;
	}
	private BookResponseV1 mapBookToBookResponseV1(Book book) {
		BookResponseV1 bookResponse = new BookResponseV1();
		bookResponse.setGenre(book.getGenre());
		bookResponse.setAsin(book.getAsin());
		bookResponse.setAuthor(book.getAuthor());
		bookResponse.setTitle(book.getTitle());
		bookResponse.setStatusCode(HttpStatus.OK);
		return bookResponse;
	}
}
