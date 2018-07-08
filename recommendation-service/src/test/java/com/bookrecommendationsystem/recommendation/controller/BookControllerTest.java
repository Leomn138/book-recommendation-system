package com.bookrecommendationsystem.recommendation.controller;

import com.bookrecommendationsystem.recommendation.dto.BookResponseV1;
import com.bookrecommendationsystem.recommendation.exception.BookNotFoundException;
import com.bookrecommendationsystem.recommendation.stub.BookStub;
import com.bookrecommendationsystem.recommendation.domain.*;
import com.bookrecommendationsystem.recommendation.service.BookService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.channels.InterruptedByTimeoutException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookControllerTest {

	@InjectMocks
	private BookController bookController;

	@Mock
	private BookService bookService;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
	}

	@Test
	public void shouldGetBookByAsin() throws Exception {

		final BookResponseV1 book = BookStub.getResponse();

		when(bookService.findByAsin(book.getAsin())).thenReturn(book);

		mockMvc.perform(get("/v1/books/" + book.getAsin()))
				.andExpect(jsonPath("$.asin").value(book.getAsin()))
				.andExpect(status().isOk());
	}

    @Test
    public void shouldReturnNotFoundWhenBookDoesNotExistOnGetBook() throws Exception {

        final Book book = BookStub.get();

        when(bookService.findByAsin(book.getAsin())).thenThrow(new BookNotFoundException());

        mockMvc.perform(get("/v1/books/" + book.getAsin()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldGetAllBooks() throws Exception {

        final BookResponseV1 book = BookStub.getResponse();

        List<BookResponseV1> bookList = new ArrayList<BookResponseV1>();
		bookList.add(book);

        when(bookService.getAll()).thenReturn(bookList);

        mockMvc.perform(get("/v1/books"))
                .andExpect(jsonPath("$.[0].asin").value(book.getAsin()))
                .andExpect(status().isOk());

    }
    
}
