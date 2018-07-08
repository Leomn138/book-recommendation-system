package com.bookrecommendationsystem.recommendation.service;

import com.bookrecommendationsystem.recommendation.domain.Book;
import com.bookrecommendationsystem.recommendation.dto.BookResponseV1;
import com.bookrecommendationsystem.recommendation.exception.BookNotFoundException;
import com.bookrecommendationsystem.recommendation.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository repository;

	@Override
	public BookResponseV1 findByAsin(String asin) throws BookNotFoundException {
		Book book = repository.findByAsin(asin);
		if (book == null) throw new BookNotFoundException();

		return mapBookToBookResponseV1(book);
	}

	@Override
	public List<BookResponseV1> getAll() {
		return new ArrayList<BookResponseV1>((Collection)repository.findAll());
	}

	private BookResponseV1 mapBookToBookResponseV1(Book book) {
		BookResponseV1 bookResponse = new BookResponseV1();
		bookResponse.setGenre(book.getGenre());
		bookResponse.setAsin(book.getAsin());
		bookResponse.setAuthor(book.getAuthor());
		bookResponse.setTitle(book.getTitle());
		return bookResponse;
	}
}
