package com.bookrecommendationsystem.recommendation.service;

import com.bookrecommendationsystem.recommendation.domain.*;
import com.bookrecommendationsystem.recommendation.dto.BookResponseV1;
import com.bookrecommendationsystem.recommendation.exception.BookNotFoundException;
import com.bookrecommendationsystem.recommendation.repository.BookRepository;
import com.bookrecommendationsystem.recommendation.stub.BookStub;
import com.bookrecommendationsystem.recommendation.stub.UserStub;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class BookServiceTest {

	@InjectMocks
	private BookServiceImpl bookService;

	@Mock
	private BookRepository repository;

	@Before
	public void setup() {
		initMocks(this);
	}

	@Test
	public void shouldFindByAsin() throws BookNotFoundException {

		final Book book = BookStub.get();

		when(repository.findByAsin(book.getAsin())).thenReturn(book);
		BookResponseV1 found = bookService.findByAsin(book.getAsin());

		assertEquals(book.getAsin(), found.getAsin());
		assertEquals(book.getAuthor(), found.getAuthor());
		assertEquals(book.getTitle(), found.getTitle());
		assertEquals(book.getGenre(), found.getGenre());

	}

	@Test(expected = BookNotFoundException.class)
	public void shouldThrowBookNotFoundExceptionWhenAsinIsEmpty() throws BookNotFoundException {
		bookService.findByAsin("");
	}

	@Test
	public void shouldFindAllBooks() {
		final Book book = BookStub.get();
		Iterable<Book> iterableBooks = Arrays.asList(book);

		when(repository.findAll()).thenReturn(iterableBooks);

		List<BookResponseV1> response = bookService.getAll();

		assertEquals(response.contains(book), true);
	}
}
