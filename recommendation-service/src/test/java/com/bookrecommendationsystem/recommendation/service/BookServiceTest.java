package com.bookrecommendationsystem.recommendation.service;

import com.bookrecommendationsystem.recommendation.domain.*;
import com.bookrecommendationsystem.recommendation.dto.BookResponseV1;
import com.bookrecommendationsystem.recommendation.repository.BookRepository;
import com.bookrecommendationsystem.recommendation.stub.BookStub;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
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
	public void bookService_FindByAsin_ShouldReturnCorrectBook() {
		final Book book = BookStub.get();
		final BookResponseV1 bookResponse = BookStub.getResponse();

		when(repository.findByAsin(any())).thenReturn(book);
		BookResponseV1 found = bookService.findByAsin(book.getAsin());

		assertEquals(bookResponse.getAsin(), found.getAsin());
		assertEquals(bookResponse.getAuthor(), found.getAuthor());
		assertEquals(bookResponse.getTitle(), found.getTitle());
		assertEquals(bookResponse.getGenre(), found.getGenre());
		assertEquals(bookResponse.getStatusCode(), found.getStatusCode());
		assertNull(found.getError());
	}

	@Test
	public void bookService_FindByAsyn_WhenAsinIsEmpty_ShouldReturnNotFoundError() {
		BookResponseV1 found = bookService.findByAsin("123");
		assertEquals(HttpStatus.NOT_FOUND, found.getStatusCode());
		assertEquals("Book asin: 123 not found.", found.getError().getMessage());
	}

	@Test
	public void bookService_FindAllBooks_ShouldReturnAllBooks() {
		final Book book = BookStub.get();
		Iterable<Book> iterableBooks = Arrays.asList(book);

		when(repository.findAll()).thenReturn(iterableBooks);

		List<BookResponseV1> response = bookService.getAll();

		assertEquals(response.contains(book), true);
	}
}
